<?php
require_once __DIR__ . '/../src/bootstrap.php';
$rascunhoBooking = $_SESSION['booking_draft'] ?? [];
if (empty($rascunhoBooking['pet_id']) || empty($rascunhoBooking['data']) || empty($rascunhoBooking['horario'])) {
    definirAviso('Complete as etapas do agendamento antes de revisar.');
    redirecionar(empty($rascunhoBooking['profissional_id']) ? 'servico.php' : 'data.php');
}
$usuarioTutor = usuarioAtual();
$petBooking = obterPet((int) $rascunhoBooking['pet_id'], (int) $usuarioTutor['id']);
$profissionalBooking = null;
foreach (profissionaisDisponiveis() as $item) { if ((int) $item['id'] === (int) $rascunhoBooking['profissional_id']) { $profissionalBooking = $item; break; } }
$horaFim = (new DateTimeImmutable($rascunhoBooking['horario']))->modify('+45 minutes')->format('H:i');
$tituloPagina = 'Revisar agendamento | FuturusCare';
$paginaAtual = 'revisao.php';
renderizarPagina('revisao.php');
