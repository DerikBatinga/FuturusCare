<?php
require_once __DIR__ . '/../src/bootstrap.php';
$tituloPagina = 'Escolher data e horário | FuturusCare';
$paginaAtual = 'data.php';
$diasSemana = ['Dom', 'Seg', 'Ter', 'Qua', 'Qui', 'Sex', 'Sáb'];
$rascunhoBooking = $_SESSION['booking_draft'] ?? [];
$dataSelecionada = !empty($rascunhoBooking['data']) ? new DateTimeImmutable($rascunhoBooking['data']) : (new DateTimeImmutable('tomorrow'))->setTime(0, 0);
$inicioMes = $dataSelecionada->modify('first day of this month');
$primeiroDia = (int) $inicioMes->format('w');
$diasNoMes = (int) $inicioMes->format('t');
$mesesPt = [1 => 'janeiro', 2 => 'fevereiro', 3 => 'março', 4 => 'abril', 5 => 'maio', 6 => 'junho', 7 => 'julho', 8 => 'agosto', 9 => 'setembro', 10 => 'outubro', 11 => 'novembro', 12 => 'dezembro'];
$mesExtenso = ucfirst($mesesPt[(int) $dataSelecionada->format('n')]) . ' de ' . $dataSelecionada->format('Y');
$petBooking = usuarioAtual() ? obterPet((int) ($rascunhoBooking['pet_id'] ?? 0), (int) usuarioAtual()['id']) : null;
$profissionalBooking = null;
foreach (profissionaisDisponiveis() as $item) {
    if ((int) $item['id'] === (int) ($rascunhoBooking['profissional_id'] ?? 0)) { $profissionalBooking = $item; break; }
}
$horarioSelecionado = (string) ($rascunhoBooking['horario'] ?? '09:00');
$enderecoBooking = (string) ($rascunhoBooking['endereco'] ?? $rascunhoBooking['local'] ?? 'Vila Mariana, São Paulo');
$servicoBooking = (string) ($rascunhoBooking['servico'] ?? 'Passeio com cães');
$dataSelecionadaTexto = dataPorExtenso($dataSelecionada->format('Y-m-d'));
$valorBooking = (float) ($rascunhoBooking['valor'] ?? ($profissionalBooking ? valorServico($profissionalBooking, $servicoBooking) : 45));
$iniciaisProfissional = implode('', array_map(static fn(string $parte): string => strtoupper(substr($parte, 0, 1)), array_slice(explode(' ', $profissionalBooking['nome'] ?? 'Profissional'), 0, 2)));
$horarios = ['08:00', '09:00', '10:00', '16:00'];
renderizarPagina('data.php');
