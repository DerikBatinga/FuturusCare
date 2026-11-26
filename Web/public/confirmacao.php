<?php
require_once __DIR__ . '/../src/bootstrap.php';
$idAgendamento = (int) ($_GET['id'] ?? $_SESSION['agendamento_atual_id'] ?? 0);
if (!$idAgendamento) {
    $listaUsuario = agendamentosDoUsuario();
    $idAgendamento = $listaUsuario ? (int) end($listaUsuario)['id'] : 0;
}
$agendamentoAtual = $idAgendamento ? buscarAgendamento($idAgendamento) : null;
if (!$agendamentoAtual) {
    definirAviso('Não encontramos esse agendamento na sua conta.');
    redirecionar('agenda.php');
}
$petAtual = null;
$profissionalAtual = null;
foreach (dados('pets') as $pet) { if ((int) $pet['id'] === (int) $agendamentoAtual['pet_id']) { $petAtual = $pet; break; } }
foreach (profissionaisDisponiveis() as $perfil) { if ((int) $perfil['id'] === (int) $agendamentoAtual['profissional_id']) { $profissionalAtual = $perfil; break; } }
$horaFim = (new DateTimeImmutable($agendamentoAtual['horario']))->modify('+45 minutes')->format('H:i');
$tituloPagina = 'Agendamento confirmado | FuturusCare';
$paginaAtual = 'confirmacao.php';
renderizarPagina('confirmacao.php');
