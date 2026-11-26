<?php
require_once __DIR__ . '/../src/bootstrap.php';
$tituloPagina = 'Entre em contato | FuturusCare';
$paginaAtual = 'contato.php';
$perguntas = [
    ['pergunta' => 'Como pedir um cuidador?', 'resposta' => 'Escolha um serviço, compare os perfis disponíveis e combine os detalhes antes de reservar.'],
    ['pergunta' => 'Como funciona o passeio?', 'resposta' => 'Você define a rotina com o profissional e acompanha as atualizações pelo diário do pet.'],
    ['pergunta' => 'Onde vejo meus agendamentos?', 'resposta' => 'Acesse Minha conta e abra a lista de agendamentos para consultar os próximos cuidados.'],
];
renderizarPagina('contato.php');
