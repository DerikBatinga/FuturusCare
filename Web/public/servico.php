<?php
require_once __DIR__ . '/../src/bootstrap.php';
$tituloPagina = 'Agendar serviço | FuturusCare';
$paginaAtual = 'servico.php';
$usuarioBooking = usuarioAtual();
$petsTutor = $usuarioBooking ? usuariosDoTutor((int) $usuarioBooking['id']) : [];
$rascunhoBooking = $_SESSION['booking_draft'] ?? [];
$petBookingId = (int) ($rascunhoBooking['pet_id'] ?? ($petsTutor[0]['id'] ?? 0));
$petAtual = null;
foreach ($petsTutor as $pet) {
    if ((int) $pet['id'] === $petBookingId) {
        $petAtual = $pet;
        break;
    }
}
$servicoBooking = (string) ($rascunhoBooking['servico'] ?? 'Passeio com cães');
$profissionalBookingId = (int) ($_GET['profissional_id'] ?? $rascunhoBooking['profissional_id'] ?? 2);
$profissionalSelecionado = null;
foreach (profissionaisDisponiveis() as $perfilDisponivel) {
    if ((int) $perfilDisponivel['id'] === $profissionalBookingId) {
        $profissionalSelecionado = $perfilDisponivel;
        break;
    }
}
$valorServicoBooking = $profissionalSelecionado ? valorServico($profissionalSelecionado, $servicoBooking) : 45;
$localBooking = (string) ($rascunhoBooking['local'] ?? ($usuarioBooking['endereco'] ?? 'Vila Mariana, São Paulo'));
$servicosDisponiveis = [
    [
        'nome' => 'Passeio com cães',
        'descricao' => 'Atividade ao ar livre, companhia e atenção ao ritmo do seu pet.',
        'detalhe' => '45 minutos',
                'valor' => moeda($profissionalSelecionado ? valorServico($profissionalSelecionado, 'Passeio com cães') : 45),
        'icone' => 'walk',
        'selecionado' => $servicoBooking === 'Passeio com cães',
    ],
    [
        'nome' => 'Pet sitter',
        'descricao' => 'Visitas em casa para alimentação, brincadeiras e companhia.',
        'detalhe' => 'Por visita',
                'valor' => 'A partir de ' . moeda($profissionalSelecionado ? valorServico($profissionalSelecionado, 'Pet sitter') : 60),
        'icone' => 'paw',
        'selecionado' => $servicoBooking === 'Pet sitter',
    ],
    [
        'nome' => 'Hospedagem',
        'descricao' => 'Um lugar acolhedor para seu pet enquanto você viaja.',
        'detalhe' => 'Por diária',
                'valor' => 'A partir de ' . moeda($profissionalSelecionado ? valorServico($profissionalSelecionado, 'Hospedagem') : 90),
        'icone' => 'home',
        'selecionado' => $servicoBooking === 'Hospedagem',
    ],
];
$servicoAtual = $servicosDisponiveis[0];
foreach ($servicosDisponiveis as $opcaoServico) {
    if ($opcaoServico['nome'] === $servicoBooking) {
        $servicoAtual = $opcaoServico;
        break;
    }
}
renderizarPagina('servico.php');
