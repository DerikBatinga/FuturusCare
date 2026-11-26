<?php
require_once __DIR__ . '/../src/bootstrap.php';
$idAgendamento = (int) ($_GET['id'] ?? $_SESSION['agendamento_atual_id'] ?? 0);
$agendamentoPagamento = $idAgendamento ? buscarAgendamento($idAgendamento) : null;
if (!$agendamentoPagamento || $agendamentoPagamento['status'] === 'Pago (simulado)') {
    definirAviso($agendamentoPagamento ? 'Este agendamento já foi pago.' : 'Selecione um agendamento para pagar.');
    redirecionar('agenda.php');
}
$petPagamento = null;
$profissionalPagamento = null;
foreach (dados('pets') as $pet) { if ((int) $pet['id'] === (int) $agendamentoPagamento['pet_id']) { $petPagamento = $pet; break; } }
foreach (profissionaisDisponiveis() as $perfil) { if ((int) $perfil['id'] === (int) $agendamentoPagamento['profissional_id']) { $profissionalPagamento = $perfil; break; } }
$tituloPagina = 'Pagamento | FuturusCare';
$paginaAtual = 'pagamento.php';
$metodosPagamento = [
    ['id' => 'pix', 'titulo' => 'Pix', 'descricao' => 'Aprovação rápida', 'selo' => 'Recomendado'],
    ['id' => 'cartao', 'titulo' => 'Cartão de crédito', 'descricao' => 'Visa final 4242', 'selo' => ''],
];
renderizarPagina('pagamento.php');
