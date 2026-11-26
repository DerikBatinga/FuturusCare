<?php require VIEW_PATH . '/partials/header.php'; ?>
<main id="conteudo" class="pagina-perfil">
    <div class="perfil-cabecalho-fundo">
        <div class="container perfil-cabecalho">
            <nav class="perfil-migalhas" aria-label="Você está aqui">
                <a href="index.php">Início</a><span aria-hidden="true">›</span>
                <a href="servicos.php">Buscar cuidadores</a><span aria-hidden="true">›</span>
                <strong><?= escapar($nomeProfissional) ?></strong>
            </nav>
            <div class="perfil-identificacao">
                <span class="perfil-avatar"><?php if (imagemPerfil($usuarioProfissional)): ?><img src="<?= escapar(imagemPerfil($usuarioProfissional)) ?>" alt="Foto de <?= escapar($nomeProfissional) ?>"><?php else: ?><?= escapar(implode('', array_map(static fn(string $parte): string => strtoupper(substr($parte, 0, 1)), array_slice(explode(' ', $nomeProfissional), 0, 2)))) ?><?php endif; ?></span>
                <div>
                    <h1><?= escapar($nomeProfissional) ?></h1>
                    <p><?= iconeSvg('pin') ?> <?= escapar($perfilProfissional['regiao']) ?></p>
                    <p><?= iconeSvg('calendar') ?> Membro FuturusCare desde <?= escapar($dataCadastroProfissional) ?></p>
                </div>
            </div>
        </div>
    </div>

    <div class="container perfil-layout">
        <div class="perfil-conteudo">
            <section class="perfil-sobre" aria-labelledby="titulo-sobre-cuidadora">
                <h2 id="titulo-sobre-cuidadora">Sobre <?= escapar($nomeProfissional) ?></h2>
                <p><?= escapar($perfilProfissional['sobre']) ?> Atualizações diárias para você ficar tranquilo(a).</p>
                <ul class="perfil-caracteristicas">
                    <?php foreach ($caracteristicas as $item): ?>
                        <li><span class="caracteristica-icone caracteristica-<?= $item['cor'] ?>"><?= iconeSvg($item['icone']) ?></span><span><?= $item['texto'] ?></span></li>
                    <?php endforeach; ?>
                </ul>
            </section>

            <section class="perfil-habilidades" aria-labelledby="titulo-habilidades">
                <h2 id="titulo-habilidades">Habilidades da cuidadora</h2>
                <ul class="perfil-tags">
                    <?php foreach ($habilidades as $habilidade): ?>
                        <li><?= htmlspecialchars($habilidade, ENT_QUOTES, 'UTF-8') ?></li>
                    <?php endforeach; ?>
                </ul>
            </section>

            <section class="perfil-galeria galeria-perfil" aria-labelledby="titulo-galeria">
                <div class="perfil-titulo-linha"><div><h2 id="titulo-galeria"><?php if ($donoPerfil && count($galeria) < 12): ?><button class="galeria-titulo-acao galeria-abrir-seletor" type="button">Galeria de fotos</button><?php else: ?>Galeria de fotos<?php endif; ?></h2><p><?= count($galeria) ?> de 12 fotos</p></div></div>
                <?php if ($galeria): ?>
                    <div class="galeria-fotos">
                        <?php foreach ($galeria as $indice => $foto): ?>
                            <figure class="galeria-item-gerenciavel">
                                <img src="<?= escapar($foto) ?>" alt="Foto <?= $indice + 1 ?> da galeria de <?= escapar($nomeProfissional) ?>">
                                <?php if ($donoPerfil): ?>
                                    <div class="galeria-item-acoes">
                                        <form class="galeria-trocar-form" method="post" action="profissionalpublico.php" enctype="multipart/form-data">
                                            <input type="hidden" name="acao" value="galeria_trocar"><?= csrfCampo() ?><input type="hidden" name="indice" value="<?= $indice ?>">
                                            <label>Trocar<input class="galeria-troca-arquivo" type="file" name="foto" accept="image/jpeg,image/png,image/webp" aria-label="Trocar foto <?= $indice + 1 ?>"></label>
                                            <button type="submit" disabled>Salvar troca</button>
                                        </form>
                                        <form method="post" action="profissionalpublico.php" data-confirmacao="Excluir esta foto da galeria?">
                                            <input type="hidden" name="acao" value="galeria_excluir"><?= csrfCampo() ?><input type="hidden" name="indice" value="<?= $indice ?>">
                                            <button type="submit">Excluir</button>
                                        </form>
                                    </div>
                                <?php endif; ?>
                            </figure>
                        <?php endforeach; ?>
                    </div>
                <?php else: ?>
                    <?php if ($donoPerfil): ?><button class="galeria-vazia galeria-abrir-seletor" type="button"><?= iconeSvg('camera') ?><span><strong>Sua galeria ainda está vazia</strong><small>Adicione imagens dos pets e dos cuidados que você oferece.</small></span></button><?php else: ?><div class="galeria-vazia"><?= iconeSvg('camera') ?><span><strong>Galeria ainda sem fotos</strong><small>Este profissional ainda não adicionou fotos ao perfil.</small></span></div><?php endif; ?>
                <?php endif; ?>
                <?php if ($donoPerfil && count($galeria) < 12): ?>
                    <form class="galeria-upload-form" method="post" action="profissionalpublico.php" enctype="multipart/form-data">
                        <input type="hidden" name="acao" value="galeria_adicionar"><?= csrfCampo() ?>
                        <input class="galeria-arquivos" type="file" name="galeria[]" accept="image/jpeg,image/png,image/webp" data-restantes="<?= 12 - count($galeria) ?>" multiple>
                        <dialog class="galeria-confirmacao" aria-labelledby="titulo-confirmar-galeria-profissional">
                            <div class="galeria-confirmacao-conteudo">
                                <h3 id="titulo-confirmar-galeria-profissional">Confirmar fotos</h3>
                                <p>Revise as imagens antes de adicioná-las à sua galeria.</p>
                                <div class="galeria-previas" aria-live="polite"></div>
                                <small>JPG, PNG ou WebP · até 5 MB por foto · máximo de 12 fotos.</small>
                                <div class="galeria-confirmacao-acoes"><button class="botao botao-secundario galeria-cancelar" type="button">Cancelar</button><button class="botao botao-primario galeria-confirmar" type="submit" disabled>Confirmar</button></div>
                            </div>
                        </dialog>
                    </form>
                <?php endif; ?>
            </section>

            <section class="perfil-localizacao" aria-labelledby="titulo-localizacao">
                <h2 id="titulo-localizacao">Localização</h2>
                <p class="perfil-endereco"><?= iconeSvg('pin') ?> <?= escapar($perfilProfissional['regiao']) ?></p>
                <img class="perfil-mapa" src="assets/img/mapa-ilustrativo.svg" alt="Mapa ilustrativo da região de <?= escapar($perfilProfissional['regiao']) ?>">
                <small>Mapa ilustrativo do bairro • localização aproximada</small>
            </section>

            <section class="perfil-avaliacoes" aria-labelledby="titulo-avaliacoes">
                <div class="perfil-titulo-linha"><h2 id="titulo-avaliacoes">Avaliações</h2><div class="galeria-controles"><button type="button" aria-label="Avaliações anteriores">‹</button><button type="button" aria-label="Próximas avaliações">›</button></div></div>
                <article class="avaliacao-cuidadora">
                    <div class="avaliacao-autora"><span class="avaliacao-iniciais">TR</span><div><strong>Thayna Reis</strong><small>Avaliação do cliente</small></div></div>
                    <div class="avaliacao-estrelas"><strong>5</strong> <span aria-label="5 de 5 estrelas">★★★★★</span></div>
                    <p>A tia Lari foi muito querida com a minha gatinha, que já é idosa e bem tímida com pessoas novas. Ela me mandou atualizações e cuidou de tudo com carinho.</p>
                    <time datetime="2026-10">Outubro de 2026</time>
                </article>
            </section>
        </div>

        <aside class="perfil-reserva" aria-label="Serviços, disponibilidade e reserva">
            <section class="perfil-servicos">
                <h2>Serviços prestados pela cuidadora</h2>
                <div class="perfil-servicos-grade">
                    <?php foreach ($servicosPerfil as $servico): ?>
                        <?php $iconeServico = ['Hospedagem' => 'home', 'Creche' => 'heart', 'Pet sitter' => 'paw', 'Passeio com cães' => 'walk'][$servico] ?? 'paw'; ?>
                        <div><span><?= iconeSvg($iconeServico) ?></span><strong><?= escapar($servico) ?></strong></div>
                    <?php endforeach; ?>
                </div>
            </section>
            <div class="perfil-preco"><strong><?= iconeSvg('card') ?> Valores</strong><span><b>R$ <?= number_format((float)$perfilProfissional['preco'], 2, ',', '.') ?></b> / atendimento</span></div>
            <section class="perfil-calendario" aria-labelledby="titulo-calendario">
                <h2 id="titulo-calendario">Hospedagem</h2>
                <p>Período da busca: 17/10/2026 a 18/10/2026</p>
                <div class="calendario-topo"><strong>Outubro 2026</strong><span aria-hidden="true">‹ &nbsp; ›</span></div>
                <div class="calendario-grade" aria-label="Calendário de outubro de 2026">
                    <?php foreach ($diasSemana as $dia): ?><span class="calendario-dia-semana"><?= $dia ?></span><?php endforeach; ?>
                    <?php for ($i = 0; $i < $primeiroDia; $i++): ?><span class="calendario-vazio" aria-hidden="true"></span><?php endfor; ?>
                    <?php for ($dia = 1; $dia <= $diasNoMes; $dia++): ?>
                        <span class="calendario-dia <?= in_array($dia, [17, 18], true) ? 'dia-selecionado' : '' ?>"><?= $dia ?></span>
                    <?php endfor; ?>
                </div>
                <div class="calendario-legenda"><span><i class="calendario-indisponivel"></i> Indisponível</span><span><i></i> Parcialmente disponível</span></div>
            </section>
            <section class="perfil-regras" aria-labelledby="titulo-regras">
                <div class="perfil-titulo-linha"><h2 id="titulo-regras">Sobre este serviço</h2><span>4 detalhes</span></div>
                <ul>
                    <li><span><?= iconeSvg('paw') ?></span><div><strong>Somente cão</strong><small>Não atende gatos</small></div></li>
                    <li><span><?= iconeSvg('paw') ?></span><div><strong>Aceita qualquer porte</strong><small>Pequeno, médio e grande</small></div></li>
                    <li><span><?= iconeSvg('shield') ?></span><div><strong>Vacinação necessária</strong><small>Carteira em dia</small></div></li>
                    <li><span><?= iconeSvg('shield') ?></span><div><strong>Castração necessária</strong><small>Só atende pets castrados</small></div></li>
                </ul>
            </section>
            <a class="botao botao-contorno botao-cheio perfil-mensagem" href="<?= $usuarioLogado ? ($usuarioLogado['tipo'] === 'profissional' ? 'chatprofissional.php' : 'mensagens.php') : 'login.php' ?>"><?= iconeSvg('chat') ?> Enviar mensagem</a>
            <a class="botao botao-primario botao-cheio perfil-reservar" href="servico.php?profissional_id=<?= (int) $perfilProfissional['usuario_id'] ?>">Reservar</a>
        </aside>
    </div>
</main>
<?php require VIEW_PATH . '/partials/footer.php'; ?>
