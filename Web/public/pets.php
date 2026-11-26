<?php
require_once __DIR__ . '/../src/bootstrap.php';
$petEditando = isset($_GET['id']) ? obterPet((int) $_GET['id'], (int) usuarioAtual()['id']) : null;
$acaoPet = $petEditando ? 'editar_pet' : 'cadastro_pet';
$tituloPagina = 'Cadastrar pet | FuturusCare';
$paginaAtual = 'pets.php';
renderizarPagina('pets.php');
