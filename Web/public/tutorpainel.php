<?php
require_once __DIR__ . '/../src/bootstrap.php';
$usuarioPerfil = usuarioAtual() ?? dados('usuarios')[0];
$petsTutor = array_values(array_filter(dados('pets'), fn($pet) => $pet['tutor_id'] === $usuarioPerfil['id']));
$petPrincipal = $petsTutor[0] ?? ['nome' => 'Seu pet', 'raca' => 'Adicione os dados do seu pet', 'idade' => ''];
$localizacaoPerfil = trim((string) ($usuarioPerfil['endereco'] ?? '')) ?: 'Localização não informada';
$dataCadastroPerfil = dataCurta((string) ($usuarioPerfil['data_cadastro'] ?? ''));
$tituloPagina = 'Meu perfil | FuturusCare';
$paginaAtual = 'tutorpainel.php';
$galeriaPet = array_values($usuarioPerfil['galeria'] ?? []);
renderizarPagina('tutorpainel.php');
