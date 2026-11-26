<?php
require_once __DIR__ . '/../src/bootstrap.php';
$usuarioRegistro = usuarioAtual();
$petsRegistro = [];
foreach (agendamentosDoUsuario($usuarioRegistro) as $atendimento) {
    foreach (dados('pets') as $pet) {
        if ((int) $pet['id'] === (int) $atendimento['pet_id']) { $petsRegistro[(int) $pet['id']] = $pet; }
    }
}
$tituloPagina = 'Novo registro | FuturusCare';
$paginaAtual = 'registro.php';
renderizarPagina('registro.php');
