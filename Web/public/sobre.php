<?php
require_once __DIR__ . '/../src/bootstrap.php';
$tituloPagina = 'Sobre nós | FuturusCare';
$paginaAtual = 'sobre.php';
$etapasCuidado = [
    ['numero' => '01', 'titulo' => 'Encontre', 'texto' => 'Explore serviços e conheça o perfil de quem vai cuidar.'],
    ['numero' => '02', 'titulo' => 'Combine', 'texto' => 'Escolha o horário e alinhe os detalhes da rotina no chat.'],
    ['numero' => '03', 'titulo' => 'Acompanhe', 'texto' => 'Veja os registros do diário e mantenha a conversa por perto.'],
];
renderizarPagina('sobre.php');
