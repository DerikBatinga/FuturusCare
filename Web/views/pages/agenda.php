<?php require VIEW_PATH . '/partials/header.php'; ?>
<main id="conteudo" class="pagina-agendamentos">
    <div class="agendamentos-container">
        <section class="agendamentos-cabecalho">
            <div><p class="agendamentos-sobretitulo">SUA ROTINA DE CUIDADOS</p><h1>Agendamentos</h1><p>Acompanhe os próximos encontros e os momentos especiais do seu pet.</p></div>
            <a class="botao botao-primario" href="servico.php">Novo agendamento</a>
        </section>

        <section class="agendamentos-calendario" aria-labelledby="titulo-mes-agendamentos">
            <div class="agendamentos-mes"><h2 id="titulo-mes-agendamentos">Outubro de 2026</h2><button type="button" aria-label="Abrir calendário">▦</button></div>
            <div class="agendamentos-dias" role="list" aria-label="Dias desta semana">
                <?php foreach ($dias as $dia): ?>
                    <div class="agendamento-dia <?= htmlspecialchars($dia['classe'] ?? '', ENT_QUOTES, 'UTF-8') ?> <?= !empty($dia['selecionado']) ? 'agendamento-dia-selecionado' : '' ?>" role="button" tabindex="0" data-dia="<?= htmlspecialchars($dia['data'], ENT_QUOTES, 'UTF-8') ?>" aria-pressed="<?= !empty($dia['selecionado']) ? 'true' : 'false' ?>" <?= !empty($dia['selecionado']) ? 'aria-current="date"' : '' ?>>
                        <span><?= $dia['dia'] ?></span><strong><?= $dia['data'] ?></strong>
                    </div>
                <?php endforeach; ?>
            </div>
        </section>

        <section class="agendamentos-compromissos" aria-labelledby="titulo-compromissos">
            <div class="agendamentos-lista-topo"><div><h2 id="titulo-compromissos">Seus compromissos</h2><p><?= count($agendamentos) ?> atendimento(s) registrado(s)</p></div><?php if ($usuarioAtual['tipo'] === 'tutor'): ?><a href="pagamento.php">Pagamentos</a><?php endif; ?></div>
            <div class="agendamentos-lista" id="filtros-agendamentos">
                <?php foreach ($agendamentos as $agendamento): ?>
                    <article class="agendamento-card">
                        <div class="agendamento-card-pessoa">
                            <span class="agendamento-avatar"><?= htmlspecialchars($agendamento['iniciais'], ENT_QUOTES, 'UTF-8') ?></span>
                            <div><h3><?= htmlspecialchars($agendamento['pessoa'], ENT_QUOTES, 'UTF-8') ?></h3><p>Pet: <?= htmlspecialchars($agendamento['pet'], ENT_QUOTES, 'UTF-8') ?></p></div>
                        </div>
                        <span class="agendamento-status agendamento-status-<?= $agendamento['classe'] ?>"><?= htmlspecialchars($agendamento['status'], ENT_QUOTES, 'UTF-8') ?></span>
                        <div class="agendamento-card-detalhes">
                            <p><?= iconeSvg('clock') ?> <?= htmlspecialchars($agendamento['quando'], ENT_QUOTES, 'UTF-8') ?></p>
                            <strong><?= htmlspecialchars($agendamento['servico'], ENT_QUOTES, 'UTF-8') ?></strong>
                        </div>
                        <div class="agendamento-card-acoes">
                            <span><?= htmlspecialchars($agendamento['valor'], ENT_QUOTES, 'UTF-8') ?></span>
                            <a href="<?= htmlspecialchars($agendamento['destino'], ENT_QUOTES, 'UTF-8') ?>">Ver detalhes</a>
                        </div>
                    </article>
                <?php endforeach; ?>
                <?php if (!$agendamentos): ?><p class="agendamentos-nota">Você ainda não tem agendamentos. Quando marcar um cuidado, ele aparecerá aqui.</p><?php endif; ?>
            </div>
            <p class="agendamentos-nota">Os horários são exibidos conforme o fuso local do atendimento.</p>
        </section>
    </div>
</main>
<?php require VIEW_PATH . '/partials/footer.php'; ?>
