<?php
require_once __DIR__ . '/../src/bootstrap.php';
$tituloPagina = 'Escolher profissional | FuturusCare';
$paginaAtual = 'cuidador.php';
$rascunhoBooking = $_SESSION['booking_draft'] ?? [];
$servicoBooking = (string) ($rascunhoBooking['servico'] ?? 'Passeio com cães');
$usuarioTutor = usuarioAtual();
$petBooking = $usuarioTutor ? obterPet((int) ($rascunhoBooking['pet_id'] ?? 0), (int) $usuarioTutor['id']) : null;
$profissionais = array_values(array_filter(profissionaisDisponiveis(), static fn(array $perfil): bool => in_array($servicoBooking, $perfil['servicos'], true)));
foreach ($profissionais as &$profissional) {
    $profissional['iniciais'] = implode('', array_map(static fn(string $parte): string => strtoupper(substr($parte, 0, 1)), array_slice(explode(' ', $profissional['nome']), 0, 2)));
    $profissional['nota'] = $profissional['nota'] ?? '4,9';
    $profissional['avaliacoes'] = $profissional['avaliacoes'] ?? '32 avaliações';
    $profissional['descricao'] = $profissional['sobre'];
    $profissional['experiencia'] = 'Atende em ' . ($profissional['regiao'] ?: 'sua região');
    $profissional['preco'] = moeda(valorServico($profissional, $servicoBooking)) . ' / atendimento';
    $profissional['selecionada'] = (int) $profissional['id'] === (int) ($rascunhoBooking['profissional_id'] ?? 0);
}
unset($profissional);
$localBooking = (string) ($rascunhoBooking['local'] ?? 'Vila Mariana, São Paulo');
renderizarPagina('cuidador.php');
