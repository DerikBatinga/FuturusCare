<?php
require_once __DIR__ . '/../src/bootstrap.php';
$tituloPagina = 'Mensagens | FuturusCare';
$paginaAtual = 'mensagens.php';
$usuarioChat = usuarioAtual();
$atendimentosChat = agendamentosDoUsuario($usuarioChat);
$conversaUsuarioId = $usuarioChat['tipo'] === 'tutor'
    ? (int) ($atendimentosChat ? $atendimentosChat[0]['profissional_id'] : 2)
    : (int) ($atendimentosChat ? $atendimentosChat[0]['tutor_id'] : 1);
$contatoChat = null;
foreach (dados('usuarios') as $contato) { if ((int) $contato['id'] === $conversaUsuarioId) { $contatoChat = $contato; break; } }
$petChat = $usuarioChat['tipo'] === 'tutor' ? (usuariosDoTutor((int) $usuarioChat['id'])[0] ?? null) : null;
if ($atendimentosChat) {
    foreach (dados('pets') as $pet) { if ((int) $pet['id'] === (int) $atendimentosChat[0]['pet_id']) { $petChat = $pet; break; } }
}
$mensagensConversa = array_values(array_filter(dados('mensagens'), static fn(array $mensagem): bool => ((int) $mensagem['de'] === (int) $usuarioChat['id'] && (int) $mensagem['para'] === $conversaUsuarioId) || ((int) $mensagem['de'] === $conversaUsuarioId && (int) $mensagem['para'] === (int) $usuarioChat['id'])));
usort($mensagensConversa, static fn(array $a, array $b): int => strcmp($a['data'], $b['data']));
renderizarPagina('mensagens.php');
