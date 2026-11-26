<?php
require_once __DIR__ . '/../src/bootstrap.php';
$usuarioPainel = usuarioAtual();
$perfilPainel = perfilProfissional((int) $usuarioPainel['id']);
$tituloPagina = 'Painel profissional | FuturusCare';
$paginaAtual = 'painel.php';
$atendimentos = [];
foreach (agendamentosDoUsuario($usuarioPainel) as $item) {
    $tutor = null; $pet = null;
    foreach (dados('usuarios') as $candidato) { if ((int) $candidato['id'] === (int) $item['tutor_id']) { $tutor = $candidato; break; } }
    foreach (dados('pets') as $candidato) { if ((int) $candidato['id'] === (int) $item['pet_id']) { $pet = $candidato; break; } }
    $atendimentos[] = ['hora' => $item['horario'], 'pet' => $pet['nome'] ?? 'Pet', 'tutor' => $tutor['nome'] ?? 'Tutor', 'servico' => $item['servico'], 'status' => $item['status']];
}
$servicos = [];
foreach ($perfilPainel['servicos'] ?? [] as $nomeServico) {
    $unidade = ['Passeio com cães' => 'por passeio', 'Pet sitter' => 'por visita', 'Hospedagem' => 'por noite', 'Creche' => 'por dia'][$nomeServico] ?? 'por atendimento';
    $servicos[] = ['nome' => $nomeServico, 'descricao' => 'Serviço disponível para os tutores da sua região.', 'preco' => moeda(valorServico($perfilPainel, $nomeServico)), 'unidade' => $unidade];
}
renderizarPagina('painel.php');
