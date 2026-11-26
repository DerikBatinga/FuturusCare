<?php
declare(strict_types=1);

$pagina = basename($_SERVER['PHP_SELF'] ?? 'index.php');
if ($tiposPermitidos = protegidaPorPerfil($pagina)) {
    exigirPerfil($tiposPermitidos, $pagina);
}

if (($_SERVER['REQUEST_METHOD'] ?? 'GET') === 'POST' && isset($_POST['acao'])) {
    $acao = trim((string) $_POST['acao']);
    $campo = static fn(string $nome): string => trim((string) ($_POST[$nome] ?? ''));
    $usuario = usuarioAtual();
    $erro = '';
    $paginaRetorno = basename($_SERVER['PHP_SELF'] ?? 'index.php');

    if (!hash_equals(csrfToken(), (string) ($_POST['csrf_token'] ?? ''))) {
        registrarErro('Sua sessão expirou. Atualize a página e tente novamente.', $paginaRetorno);
    }

    if ($acao === 'login') {
        $email = strtolower($campo('email'));
        $senha = (string) ($_POST['senha'] ?? '');
        $encontrado = null;
        foreach (dados('usuarios') as $candidato) {
            if (strcasecmp((string) $candidato['email'], $email) === 0 && password_verify($senha, (string) $candidato['senha'])) {
                $encontrado = $candidato;
                break;
            }
        }
        if (!$encontrado) {
            $erro = 'E-mail ou senha não conferem. Tente novamente.';
        } else {
            session_regenerate_id(true);
            unset($_SESSION['csrf_token']);
            $_SESSION['usuario_id'] = (int) $encontrado['id'];
            $_SESSION['perfil'] = $encontrado['tipo'];
            definirAviso('Olá, ' . $encontrado['nome'] . '! Você entrou na sua conta.');
            $destino = (string) ($_SESSION['destino_depois_login'] ?? '');
            unset($_SESSION['destino_depois_login']);
            $rotasSeguras = array_keys(array_filter([
                'tutorpainel.php' => ['tutor'], 'agenda.php' => ['tutor'], 'servico.php' => ['tutor'],
                'painel.php' => ['profissional'], 'diario.php' => ['tutor', 'profissional'],
            ]));
            if ($destino && in_array($destino, $rotasSeguras, true) && in_array($encontrado['tipo'], protegidaPorPerfil($destino) ?? [], true)) {
                redirecionar($destino);
            }
            redirecionar($encontrado['tipo'] === 'profissional' ? 'painel.php' : 'tutorpainel.php');
        }
    } elseif (in_array($acao, ['cadastro_tutor', 'cadastro_profissional'], true)) {
        $email = strtolower($campo('email'));
        $senha = (string) ($_POST['senha'] ?? '');
        $tipo = $acao === 'cadastro_tutor' ? 'tutor' : 'profissional';
        if ($campo('nome') === '') {
            $erro = 'Informe seu nome completo.';
        } elseif (!filter_var($email, FILTER_VALIDATE_EMAIL)) {
            $erro = 'Informe um e-mail válido.';
        } elseif ($senha !== (string) ($_POST['confirmar-senha'] ?? '')) {
            $erro = 'As senhas precisam ser iguais.';
        } elseif (strlen($senha) < 8) {
            $erro = 'A senha precisa ter pelo menos 8 caracteres.';
        } elseif (!isset($_POST['termos'])) {
            $erro = 'Aceite os termos para criar sua conta.';
        } elseif (array_filter(dados('usuarios'), static fn(array $item): bool => strcasecmp($item['email'], $email) === 0)) {
            $erro = 'Este e-mail já tem uma conta.';
        } elseif ($tipo === 'profissional' && ($campo('regiao') === '' || $campo('servico') === '')) {
            $erro = 'Informe sua região e o serviço principal.';
        } else {
            $id = proximoId('usuarios');
            $_SESSION['fc']['usuarios'][] = [
                'id' => $id, 'nome' => $campo('nome'), 'email' => $email,
                'telefone' => $campo('telefone'), 'tipo' => $tipo,
                'data_cadastro' => date('Y-m-d'),
                'senha' => password_hash($senha, PASSWORD_DEFAULT),
                'endereco' => '', 'foto' => '', 'galeria' => [],
            ];
            if ($tipo === 'profissional') {
                $servico = $campo('servico');
                $servico = ['Passeios' => 'Passeio com cães', 'Pet sitting' => 'Pet sitter'][$servico] ?? $servico;
                $_SESSION['fc']['profissionais'][] = [
                    'id' => $id, 'usuario_id' => $id, 'regiao' => $campo('regiao'),
                    'sobre' => $campo('apresentacao'), 'servicos' => [$servico],
                    'preco' => 45, 'precos' => [$servico => 45], 'dias' => [], 'galeria' => [],
                ];
            }
            session_regenerate_id(true);
            unset($_SESSION['csrf_token']);
            $_SESSION['usuario_id'] = $id;
            $_SESSION['perfil'] = $tipo;
            definirAviso('Conta criada com sucesso. Bem-vindo à FuturusCare!');
            redirecionar($tipo === 'profissional' ? 'painel.php' : 'tutorpainel.php');
        }
    } elseif (in_array($acao, ['cadastro_pet', 'editar_pet'], true)) {
        $idade = filter_var($campo('idade'), FILTER_VALIDATE_INT, ['options' => ['min_range' => 0, 'max_range' => 100]]);
        $peso = filter_var(str_replace(',', '.', $campo('peso')), FILTER_VALIDATE_FLOAT);
        $fotoPet = [];
        try {
            $fotoPet = salvarImagensEnviadas('foto', 'pets', 1);
        } catch (RuntimeException $excecao) {
            $erro = $excecao->getMessage();
        }
        if (!$usuario || $usuario['tipo'] !== 'tutor') {
            $erro = 'Entre como tutor para cadastrar um pet.';
        } elseif ($erro === '' && ($campo('nome') === '' || $campo('especie') === '' || $idade === false || $peso === false || $peso <= 0)) {
            $erro = 'Preencha nome, espécie, idade e peso do pet.';
        } elseif ($erro === '') {
            $petId = (int) ($_POST['pet_id'] ?? 0);
            $petExistente = $acao === 'editar_pet' ? obterPet($petId, (int) $usuario['id']) : null;
            if ($acao === 'editar_pet' && !$petExistente) {
                $erro = 'Esse pet não está cadastrado na sua conta.';
            } else {
                $novoPet = [
                'id' => $acao === 'editar_pet' ? $petId : proximoId('pets'), 'tutor_id' => (int) $usuario['id'],
                'nome' => $campo('nome'), 'especie' => $campo('especie'),
                'raca' => $campo('raca'), 'idade' => $idade, 'peso' => $peso,
                'observacoes' => $campo('observacoes'),
                'foto' => $fotoPet[0] ?? ($petExistente['foto'] ?? ''),
                ];
                if ($acao === 'editar_pet') {
                    if ($fotoPet && !empty($petExistente['foto'])) {
                        removerImagemGerenciada((string) $petExistente['foto']);
                    }
                    foreach ($_SESSION['fc']['pets'] as &$itemPet) { if ((int) $itemPet['id'] === $petId && (int) $itemPet['tutor_id'] === (int) $usuario['id']) { $itemPet = $novoPet; break; } }
                    unset($itemPet);
                } else {
                    $_SESSION['fc']['pets'][] = $novoPet;
                }
                definirAviso($acao === 'editar_pet' ? 'Dados do pet atualizados.' : 'Pet cadastrado com sucesso.');
                redirecionar('tutorpainel.php');
            }
        }
    } elseif (in_array($acao, ['editar_perfil', 'editar_perfil_profissional'], true)) {
        if (!$usuario) {
            $erro = 'Entre na sua conta para editar o perfil.';
        } else {
            $email = strtolower($campo('email'));
            $emailEmUso = array_filter(dados('usuarios'), static fn(array $item): bool => (int) $item['id'] !== (int) $usuario['id'] && strcasecmp($item['email'], $email) === 0);
            $nomesServicos = ['passeio' => 'Passeio com cães', 'pet-sitting' => 'Pet sitter', 'hospedagem' => 'Hospedagem', 'creche' => 'Creche'];
            $idsServicos = array_values(array_unique(array_map('strval', (array) ($_POST['servicos'] ?? []))));
            $idsServicos = array_values(array_filter($idsServicos, static fn(string $id): bool => isset($nomesServicos[$id])));
            $fotoPerfilNova = [];
            try {
                $fotoPerfilNova = salvarImagensEnviadas('foto', 'perfis', 1);
            } catch (RuntimeException $excecao) {
                foreach ($fotoPerfilNova as $arquivoSalvo) {
                    $caminhoSalvo = dirname(__DIR__) . '/' . $arquivoSalvo;
                    if (is_file($caminhoSalvo)) {
                        @unlink($caminhoSalvo);
                    }
                }
                $erro = $excecao->getMessage();
            }
            if ($erro === '' && ($campo('nome') === '' || !filter_var($email, FILTER_VALIDATE_EMAIL))) {
                $erro = 'Informe um nome e um e-mail válido.';
            } elseif ($erro === '' && $emailEmUso) {
                $erro = 'Este e-mail já está sendo usado por outra conta.';
            } elseif ($erro === '' && $campo('senha') !== '' && strlen((string) $_POST['senha']) < 8) {
                $erro = 'A nova senha precisa ter pelo menos 8 caracteres.';
            } elseif ($erro === '' && $acao === 'editar_perfil_profissional' && ($campo('regiao') === '' || !$idsServicos)) {
                $erro = 'Informe a região atendida e selecione ao menos um serviço.';
            } elseif ($erro === '') {
                foreach ($_SESSION['fc']['usuarios'] as &$item) {
                    if ((int) $item['id'] !== (int) $usuario['id']) {
                        continue;
                    }
                    $item['nome'] = $campo('nome');
                    $item['email'] = $email;
                    $item['telefone'] = $campo('telefone') ?: ($item['telefone'] ?? '');
                    if ($fotoPerfilNova) {
                        removerImagemGerenciada((string) ($item['foto'] ?? ''));
                        $item['foto'] = $fotoPerfilNova[0];
                    }
                    if (array_key_exists('endereco', $_POST)) {
                        $item['endereco'] = $campo('endereco');
                    }
                    if ($campo('senha') !== '') {
                        $item['senha'] = password_hash((string) $_POST['senha'], PASSWORD_DEFAULT);
                    }
                }
                unset($item);
                if ($acao === 'editar_perfil_profissional') {
                    foreach ($_SESSION['fc']['profissionais'] as &$perfil) {
                        if ((int) $perfil['usuario_id'] !== (int) $usuario['id']) {
                            continue;
                        }
                        $perfil['regiao'] = $campo('regiao');
                        $perfil['sobre'] = $campo('sobre');
                        $perfil['servicos'] = array_values(array_map(static fn(string $id): string => $nomesServicos[$id], $idsServicos));
                        $diasValidos = ['segunda-feira', 'terça-feira', 'quarta-feira', 'quinta-feira', 'sexta-feira', 'sábado', 'domingo'];
                        $perfil['dias'] = array_values(array_intersect($diasValidos, array_unique(array_map('strval', (array) ($_POST['dias'] ?? [])))));
                        foreach ($idsServicos as $idServico) {
                            $servico = $nomesServicos[$idServico] ?? $idServico;
                            $preco = filter_var($_POST['preco_' . $idServico] ?? null, FILTER_VALIDATE_FLOAT);
                            if ($preco !== false && $preco >= 0 && $preco <= 100000) {
                                $perfil['precos'][$servico] = (float) $preco;
                            }
                        }
                        $perfil['preco'] = $perfil['precos'][$perfil['servicos'][0] ?? ''] ?? ($perfil['preco'] ?? 45);
                    }
                    unset($perfil);
                }
                definirAviso('Perfil atualizado com sucesso.');
                redirecionar($paginaRetorno);
            }
        }
    } elseif (in_array($acao, ['galeria_adicionar', 'galeria_trocar', 'galeria_excluir'], true)) {
        $tipoEsperado = $paginaRetorno === 'tutorpainel.php' ? 'tutor' : 'profissional';
        if (!$usuario || !in_array($paginaRetorno, ['tutorpainel.php', 'profissionalpublico.php'], true)
            || ($usuario['tipo'] ?? '') !== $tipoEsperado) {
            $erro = 'Você não pode alterar esta galeria.';
        } else {
            $galeria = galeriaDoUsuario($usuario);
            $indice = filter_var($_POST['indice'] ?? null, FILTER_VALIDATE_INT);
            try {
                if ($acao === 'galeria_adicionar') {
                    $restantes = 12 - count($galeria);
                    $novasFotos = salvarImagensEnviadas('galeria', 'galerias', $restantes);
                    if (!$novasFotos) {
                        throw new RuntimeException('Selecione ao menos uma foto para adicionar.');
                    }
                    $galeria = array_merge($galeria, $novasFotos);
                    $mensagemGaleria = count($novasFotos) === 1 ? 'Foto adicionada à galeria.' : 'Fotos adicionadas à galeria.';
                } elseif ($indice === false || !isset($galeria[$indice])) {
                    throw new RuntimeException('Não encontramos essa foto na sua galeria.');
                } elseif ($acao === 'galeria_trocar') {
                    $novaFoto = salvarImagensEnviadas('foto', 'galerias', 1);
                    if (!$novaFoto) {
                        throw new RuntimeException('Selecione a nova foto.');
                    }
                    $fotoAnterior = $galeria[$indice];
                    $galeria[$indice] = $novaFoto[0];
                    removerImagemGerenciada((string) $fotoAnterior);
                    $mensagemGaleria = 'Foto da galeria substituída.';
                } else {
                    $fotoExcluida = $galeria[$indice];
                    array_splice($galeria, $indice, 1);
                    removerImagemGerenciada((string) $fotoExcluida);
                    $mensagemGaleria = 'Foto excluída da galeria.';
                }
                definirGaleriaDoUsuario($usuario, $galeria);
                definirAviso($mensagemGaleria);
                redirecionar($paginaRetorno);
            } catch (RuntimeException $excecao) {
                $erro = $excecao->getMessage();
            }
        }
    } elseif ($acao === 'escolher_servico') {
        $petId = (int) ($_POST['pet_id'] ?? 0);
        $pet = $usuario ? obterPet($petId, (int) $usuario['id']) : null;
        $servico = $campo('servico');
        $validos = ['Passeio com cães', 'Pet sitter', 'Hospedagem'];
        $profissionalId = (int) ($_POST['profissional_id'] ?? 2);
        $profissionalInicial = null;
        foreach (profissionaisDisponiveis() as $perfil) {
            if ((int) $perfil['id'] === $profissionalId && in_array($servico, $perfil['servicos'], true)) { $profissionalInicial = $perfil; break; }
        }
        if (!$profissionalInicial) {
            foreach (profissionaisDisponiveis() as $perfil) { if (in_array($servico, $perfil['servicos'], true)) { $profissionalInicial = $perfil; break; } }
        }
        if (!$pet || !in_array($servico, $validos, true) || !$profissionalInicial) {
            $erro = 'Escolha um serviço e um pet cadastrado na sua conta.';
        } else {
            $_SESSION['booking_draft'] = [
                'tutor_id' => (int) $usuario['id'], 'pet_id' => $petId,
                'servico' => $servico, 'local' => $campo('local'),
                'profissional_id' => (int) $profissionalInicial['id'], 'valor' => valorServico($profissionalInicial, $servico),
                'data' => '', 'horario' => '', 'endereco' => $campo('endereco'),
            ];
            definirAviso('Serviço e pet selecionados. Escolha um profissional.');
            redirecionar('cuidador.php');
        }
    } elseif ($acao === 'selecionar_profissional') {
        $rascunho = $_SESSION['booking_draft'] ?? [];
        $profissionalId = (int) ($_POST['profissional_id'] ?? 0);
        $encontrado = false;
        foreach (profissionaisDisponiveis() as $profissional) {
            if ((int) $profissional['id'] === $profissionalId && in_array($rascunho['servico'] ?? '', $profissional['servicos'], true)) {
                $encontrado = true;
                break;
            }
        }
        if (!$encontrado) {
            $erro = 'Selecione um profissional disponível para este serviço.';
        } else {
            $_SESSION['booking_draft']['profissional_id'] = $profissionalId;
            $_SESSION['booking_draft']['valor'] = valorServico($profissional, (string) $rascunho['servico']);
            definirAviso('Profissional selecionado. Agora escolha a data e o horário.');
            redirecionar('data.php');
        }
    } elseif ($acao === 'selecionar_data') {
        $rascunho = $_SESSION['booking_draft'] ?? [];
        $data = $campo('data');
        $horario = $campo('horario');
        $endereco = $campo('endereco');
        $dataValida = DateTimeImmutable::createFromFormat('!Y-m-d', $data);
        $horariosValidos = ['08:00', '09:00', '10:00', '16:00'];
        if (empty($rascunho['pet_id']) || !$dataValida || $dataValida->format('Y-m-d') !== $data || !in_array($horario, $horariosValidos, true)) {
            $erro = 'Escolha uma data e um horário disponíveis.';
        } elseif ($dataValida < new DateTimeImmutable('today')) {
            $erro = 'Escolha uma data de hoje em diante.';
        } elseif ($endereco === '') {
            $erro = 'Informe o endereço do atendimento.';
        } else {
            $profissionalData = null;
            foreach (profissionaisDisponiveis() as $perfil) { if ((int) $perfil['id'] === (int) ($rascunho['profissional_id'] ?? 0)) { $profissionalData = $perfil; break; } }
            $diasPt = [1 => 'segunda-feira', 2 => 'terça-feira', 3 => 'quarta-feira', 4 => 'quinta-feira', 5 => 'sexta-feira', 6 => 'sábado', 7 => 'domingo'];
            $diaDaSemana = $diasPt[(int) $dataValida->format('N')];
            $conflito = horarioOcupado((int) ($rascunho['profissional_id'] ?? 0), $data, $horario);
            if ($conflito) {
                $erro = 'Esse horário acabou de ser reservado. Escolha outro horário.';
            } elseif ($profissionalData && !empty($profissionalData['dias']) && !in_array($diaDaSemana, $profissionalData['dias'], true)) {
                $erro = 'Esse profissional não atende no dia escolhido.';
            } else {
                $_SESSION['booking_draft']['data'] = $data;
                $_SESSION['booking_draft']['horario'] = $horario;
                $_SESSION['booking_draft']['endereco'] = $endereco;
                definirAviso('Data e horário selecionados. Revise os detalhes do agendamento.');
                redirecionar('revisao.php');
            }
        }
    } elseif ($acao === 'confirmar_agendamento') {
        $rascunho = $_SESSION['booking_draft'] ?? [];
        $pet = $usuario ? obterPet((int) ($rascunho['pet_id'] ?? 0), (int) $usuario['id']) : null;
        $profissional = null;
        foreach (profissionaisDisponiveis() as $item) {
            if ((int) $item['id'] === (int) ($rascunho['profissional_id'] ?? 0)) {
                $profissional = $item;
                break;
            }
        }
        $dataAgendamento = (string) ($rascunho['data'] ?? '');
        $horarioAgendamento = (string) ($rascunho['horario'] ?? '');
        $dataConfirmacao = DateTimeImmutable::createFromFormat('!Y-m-d', $dataAgendamento);
        $diaConfirmacao = $dataConfirmacao ? [1 => 'segunda-feira', 2 => 'terça-feira', 3 => 'quarta-feira', 4 => 'quinta-feira', 5 => 'sexta-feira', 6 => 'sábado', 7 => 'domingo'][(int) $dataConfirmacao->format('N')] : '';
        $horarioValido = in_array($horarioAgendamento, ['08:00', '09:00', '10:00', '16:00'], true);
        $diaDisponivel = $profissional && (empty($profissional['dias']) || in_array($diaConfirmacao, $profissional['dias'], true));
        if (!$usuario || $usuario['tipo'] !== 'tutor' || !$pet || !$profissional || !$dataConfirmacao
            || $dataConfirmacao->format('Y-m-d') !== $dataAgendamento || $dataConfirmacao < new DateTimeImmutable('today')
            || !$horarioValido || !$diaDisponivel) {
            $erro = 'Complete todas as etapas antes de confirmar o agendamento.';
        } elseif (horarioOcupado((int) $profissional['id'], $dataAgendamento, $horarioAgendamento)) {
            $erro = 'Esse horário acabou de ser reservado. Volte e escolha outro horário.';
        } else {
            $id = proximoId('agendamentos');
            $agendamento = [
                'id' => $id, 'codigo' => 'FC-' . date('ym') . '-' . str_pad((string) $id, 4, '0', STR_PAD_LEFT),
                'tutor_id' => (int) $usuario['id'], 'profissional_id' => (int) $profissional['id'],
                'pet_id' => (int) $pet['id'], 'servico' => $rascunho['servico'],
                'data' => $dataAgendamento, 'horario' => $horarioAgendamento,
                'endereco' => $rascunho['endereco'], 'valor' => (float) ($rascunho['valor'] ?? valorServico($profissional, $rascunho['servico'])),
                'status' => 'Aguardando pagamento',
            ];
            salvarAgendamento($agendamento);
            $_SESSION['agendamento_atual_id'] = $id;
            unset($_SESSION['booking_draft']);
            definirAviso('Agendamento registrado. Você pode concluir o pagamento quando quiser.');
            redirecionar('pagamento.php?id=' . $id);
        }
    } elseif ($acao === 'pagamento') {
        $id = (int) ($_POST['agendamento_id'] ?? $_SESSION['agendamento_atual_id'] ?? 0);
        $metodo = $campo('metodo-pagamento');
        $agendamento = buscarAgendamento($id, $usuario);
        if (!$agendamento || $usuario['tipo'] !== 'tutor') {
            $erro = 'Não encontramos esse agendamento na sua conta.';
        } elseif (!in_array($metodo, ['pix', 'cartao'], true)) {
            $erro = 'Escolha uma forma de pagamento.';
        } elseif ($agendamento['status'] === 'Pago (simulado)') {
            definirAviso('Este agendamento já está marcado como pago. Nenhuma cobrança foi feita.');
            redirecionar('agenda.php');
        } else {
            $_SESSION['fc']['pagamentos'][] = [
                'id' => proximoId('pagamentos'), 'agendamento_id' => $id,
                'metodo' => $metodo, 'valor' => (float) $agendamento['valor'],
                'status' => 'simulado', 'criado_em' => date('Y-m-d H:i:s'),
            ];
            salvarAgendamento(array_merge($agendamento, ['status' => 'Pago (simulado)']));
            definirAviso('Pagamento simulado. Nenhuma cobrança foi feita.');
            redirecionar('agenda.php');
        }
    } elseif ($acao === 'mensagem') {
        $texto = $campo('mensagem');
        $paginaChat = $usuario && $usuario['tipo'] === 'profissional' ? 'chatprofissional.php' : 'mensagens.php';
        $destinatario = $usuario && $usuario['tipo'] === 'profissional' ? 1 : (int) ($_SESSION['booking_draft']['profissional_id'] ?? 2);
        if ($usuario && $usuario['tipo'] === 'profissional') {
            $atendimentos = agendamentosDoUsuario($usuario);
            if ($atendimentos) {
                $destinatario = (int) end($atendimentos)['tutor_id'];
            }
        } elseif ($usuario) {
            $atendimentos = agendamentosDoUsuario($usuario);
            if ($atendimentos) {
                $destinatario = (int) end($atendimentos)['profissional_id'];
            }
        }
        if ($texto === '' || comprimentoTexto($texto) > 2000) {
            $erro = 'Escreva uma mensagem de até 2.000 caracteres.';
        } else {
            $_SESSION['fc']['mensagens'][] = [
                'id' => proximoId('mensagens'), 'de' => (int) $usuario['id'],
                'para' => $destinatario, 'texto' => $texto,
                'data' => date('Y-m-d H:i'),
            ];
            definirAviso('Mensagem enviada para a conversa.');
            redirecionar($paginaChat);
        }
    } elseif ($acao === 'novo_registro') {
        $petId = (int) ($_POST['pet_id'] ?? 0);
        $pet = null;
        if ($usuario['tipo'] === 'tutor') {
            $pet = obterPet($petId, (int) $usuario['id']);
        } elseif (temParticipacaoNoPet($petId, (int) $usuario['id'])) {
            foreach (dados('pets') as $candidato) {
                if ((int) $candidato['id'] === $petId) {
                    $pet = $candidato;
                    break;
                }
            }
        }
        $data = $campo('data');
        $titulo = $campo('titulo');
        $relato = $campo('relato');
        $dataRegistro = DateTimeImmutable::createFromFormat('!Y-m-d', $data);
        $categoriasValidas = ['Bem-estar', 'Alimentação', 'Água', 'Passeio', 'Saúde'];
        if (!$pet || $titulo === '' || comprimentoTexto($titulo) > 120 || $relato === ''
            || comprimentoTexto($relato) > 1000 || !$dataRegistro || $dataRegistro->format('Y-m-d') !== $data
            || !in_array($campo('categoria') ?: 'Bem-estar', $categoriasValidas, true)) {
            $erro = 'Selecione um pet e preencha o título e o relato (até 1.000 caracteres).';
        } else {
            $_SESSION['fc']['diario'][] = [
                'id' => proximoId('diario'), 'pet_id' => $petId,
                'autor_id' => (int) $usuario['id'], 'categoria' => $campo('categoria') ?: 'Bem-estar',
                'titulo' => $titulo, 'relato' => $relato,
                'data' => $data ?: date('Y-m-d'), 'horario' => $campo('horario') ?: date('H:i'),
            ];
            definirAviso('Registro adicionado ao diário compartilhado.');
            redirecionar('diario.php');
        }
    } elseif ($acao === 'contato') {
        if (!filter_var($campo('email'), FILTER_VALIDATE_EMAIL) || $campo('nome') === ''
            || comprimentoTexto($campo('nome')) > 120 || comprimentoTexto($campo('assunto')) > 160
            || $campo('mensagem') === '' || comprimentoTexto($campo('mensagem')) > 3000) {
            $erro = 'Preencha seu nome, um e-mail válido e a mensagem.';
        } else {
            $_SESSION['fc']['contatos'] ??= [];
            $_SESSION['fc']['contatos'][] = [
                'id' => proximoId('contatos'), 'nome' => $campo('nome'),
                'email' => strtolower($campo('email')), 'assunto' => $campo('assunto'),
                'mensagem' => $campo('mensagem'), 'criado_em' => date('Y-m-d H:i:s'),
            ];
            definirAviso('Mensagem registrada. Nenhum e-mail foi enviado.');
            redirecionar('contato.php');
        }
    } elseif ($acao === 'recuperar_senha') {
        if (!filter_var($campo('email'), FILTER_VALIDATE_EMAIL)) {
            $erro = 'Informe um e-mail válido.';
        } else {
            definirAviso('Se o e-mail estiver cadastrado, as instruções aparecerão em breve. Nenhum e-mail foi enviado.');
            redirecionar('senha.php');
        }
    } elseif ($acao === 'logout') {
        $_SESSION = [];
        session_regenerate_id(true);
        $_SESSION['fc'] = require APP_ROOT . '/src/Repositories/mock.php';
        definirAviso('Você saiu da sua conta.');
        redirecionar('index.php');
    } else {
        $erro = 'A ação solicitada não é reconhecida.';
    }

    if ($erro !== '') {
        registrarErro($erro, $paginaRetorno);
    }
}
