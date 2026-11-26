<?php require VIEW_PATH . '/partials/header.php'; ?>
<main id="conteudo" class="pagina-agendamento">
    <div class="booking-container">
        <section class="booking-busca" aria-labelledby="titulo-booking">
                <h1 id="titulo-booking">Vamos marcar o passeio de <?= escapar($petBooking['nome'] ?? 'seu pet') ?>?</h1>
            <form class="booking-filtros" method="post" action="servico.php">
                <input type="hidden" name="acao" value="escolher_servico">
                <?= csrfCampo() ?>
                <input type="hidden" name="profissional_id" value="<?= (int) ($rascunhoBooking['profissional_id'] ?? 2) ?>">
                <label>Serviço
                    <select name="servico"><option><?= escapar($servicoBooking) ?></option></select>
                </label>
                <label>Local
                    <input type="text" name="local" value="<?= escapar($rascunhoBooking['local'] ?? $enderecoBooking) ?>" aria-label="Local do atendimento">
                </label>
                <label>Data
                    <input type="text" name="data" value="<?= escapar($dataSelecionada->format('d/m/Y') . ' · ' . $horarioSelecionado) ?>" aria-label="Data e horário do atendimento" readonly>
                </label>
                <label>Pet
                    <select name="pet_id"><option value="<?= (int) ($petBooking['id'] ?? 0) ?>"><?= escapar($petBooking['nome'] ?? 'Escolha um pet') ?></option></select>
                </label>
                <button class="botao botao-primario" type="submit">Editar busca</button>
            </form>
        </section>

        <nav class="booking-etapas" aria-label="Etapas do agendamento">
            <span class="etapa-concluida"><b>✓</b> Serviço</span>
            <span class="etapa-concluida"><b>✓</b> Profissional</span>
            <span class="etapa-atual"><b>3</b> Data e horário</span>
            <span><b>4</b> Revisão</span>
        </nav>

        <div class="booking-conteudo booking-data-conteudo">
            <div class="booking-data-principal">
                <section class="booking-calendario-horarios" aria-labelledby="titulo-calendario-booking">
                    <div class="booking-profissional-escolhido">
                        <span class="booking-profissional-avatar"><?php if (!empty($profissionalBooking['foto'])): ?><img src="<?= escapar($profissionalBooking['foto']) ?>" alt="Foto de <?= escapar($profissionalBooking['nome']) ?>"><?php else: ?><?= escapar($iniciaisProfissional) ?><?php endif; ?></span>
                        <div><strong>Agendar com <?= escapar($profissionalBooking['nome'] ?? 'profissional') ?></strong><small><?= escapar($servicoBooking) ?></small></div>
                    </div>
                    <div class="booking-calendario-layout">
                        <div class="booking-calendario">
                    <h2 id="titulo-calendario-booking"><?= escapar($mesExtenso) ?></h2>
                            <div class="booking-calendario-grade">
                                <?php foreach ($diasSemana as $dia): ?><span class="booking-calendario-dia-semana"><?= $dia ?></span><?php endforeach; ?>
                                <?php for ($i = 0; $i < $primeiroDia; $i++): ?><span class="booking-calendario-vazio" aria-hidden="true"></span><?php endfor; ?>
                                <?php for ($dia = 1; $dia <= $diasNoMes; $dia++): ?>
                                    <?php $dataDia = $inicioMes->setDate((int) $inicioMes->format('Y'), (int) $inicioMes->format('n'), $dia); $diaAtivo = $dataDia->format('Y-m-d') === $dataSelecionada->format('Y-m-d'); ?>
                                    <button type="button" class="booking-calendario-dia <?= $diaAtivo ? 'booking-dia-selecionado' : '' ?>" data-dia="<?= $dia ?>" data-data="<?= $dataDia->format('Y-m-d') ?>" aria-pressed="<?= $diaAtivo ? 'true' : 'false' ?>" <?= $dataDia < new DateTimeImmutable('today') ? 'disabled' : '' ?>><?= $dia ?></button>
                                <?php endfor; ?>
                            </div>
                            <small class="booking-data-selecionada">Selecionado: <?= escapar($dataSelecionadaTexto) ?></small>
                        </div>
                        <div class="booking-horarios">
                            <h2>Horários disponíveis</h2>
                            <p><?= escapar($dataSelecionada->format('d/m/Y')) ?></p>
                            <div class="booking-horarios-grade">
                                <?php foreach ($horarios as $horario): ?>
                                    <button type="button" data-horario="<?= escapar($horario) ?>" class="<?= $horario === $horarioSelecionado ? 'horario-escolhido' : '' ?>" aria-pressed="<?= $horario === $horarioSelecionado ? 'true' : 'false' ?>"><?= escapar($horario) ?></button>
                                <?php endforeach; ?>
                            </div>
                            <div class="booking-horario-destaque"><strong><?= iconeSvg('clock') ?> <?= escapar($horarioSelecionado) ?>–<?= escapar(substr($horarioSelecionado, 0, 2) . ':45') ?></strong><small><?= escapar($servicoBooking) ?></small></div>
                        </div>
                    </div>
                </section>

                <section class="booking-endereco" aria-labelledby="titulo-endereco-booking">
                    <h2 id="titulo-endereco-booking">Onde atender <?= escapar($petBooking['nome'] ?? 'seu pet') ?>?</h2>
                    <label class="booking-endereco-campo" for="endereco-atendimento">
                        <span class="sr-only">Endereço de atendimento</span>
                        <input id="endereco-atendimento" type="text" name="endereco" form="form-data-booking" value="<?= escapar($enderecoBooking) ?>" autocomplete="street-address" required>
                    </label>
                    <p>O atendimento de <?= escapar($petBooking['nome'] ?? 'seu pet') ?> será realizado neste endereço.</p>
                </section>
                <a class="booking-voltar-servicos" href="cuidador.php">Voltar para profissionais</a>
            </div>

            <aside class="booking-resumo booking-resumo-data" aria-labelledby="titulo-resumo-booking">
                <h2 id="titulo-resumo-booking">Seu agendamento</h2>
                <div class="booking-pet-resumo">
                    <span class="booking-pet-imagem"><img src="<?= escapar($petBooking ? imagemPet($petBooking) : 'assets/img/pet-home-destaque.png') ?>" alt="<?= escapar($petBooking['nome'] ?? 'Pet') ?>"></span>
                    <span><strong><?= escapar($petBooking['nome'] ?? 'Seu pet') ?></strong><small><?= escapar(($petBooking['raca'] ?? '') . (isset($petBooking['idade']) ? ' · ' . $petBooking['idade'] . ' anos' : '')) ?></small></span>
                </div>
                <div class="booking-resumo-servico">
                    <strong><?= escapar($servicoBooking) ?></strong>
                    <p>Um atendimento com atenção à rotina do seu pet.</p>
                    <div><span class="booking-ana-mini"><?= escapar($iniciaisProfissional) ?></span><span><?= escapar($profissionalBooking['nome'] ?? 'Profissional') ?><br><small>Profissional selecionado</small></span></div>
                    <div class="booking-data-linha"><?= iconeSvg('calendar') ?><span><?= escapar($dataSelecionadaTexto) ?><br><small><?= escapar($horarioSelecionado) ?></small></span></div>
                    <div><span><?= escapar($servicoBooking) ?></span><strong><?= escapar(moeda($valorBooking)) ?></strong></div>
                </div>
                <div class="booking-total"><strong>Total</strong><b><?= escapar(moeda($valorBooking)) ?></b></div>
                <form id="form-data-booking" class="booking-data-form" method="post" action="data.php">
                    <input type="hidden" name="acao" value="selecionar_data">
                    <?= csrfCampo() ?>
                    <input type="hidden" name="data" value="<?= escapar($dataSelecionada->format('Y-m-d')) ?>">
                    <input type="hidden" name="horario" value="<?= escapar($horarioSelecionado) ?>">
                    <button class="botao botao-primario botao-cheio" type="submit">Revisar agendamento</button>
                </form>
                <p class="booking-observacao">Você poderá revisar os detalhes antes de confirmar.</p>
            </aside>

            <aside class="booking-dica booking-dica-data">
                <?= iconeSvg('heart') ?>
                <div><h2>Um cuidado pensado para <?= escapar($petBooking['nome'] ?? 'seu pet') ?></h2><p>Compartilhe a rotina e os cuidados do seu pet com o profissional pelo chat.</p></div>
            </aside>
        </div>
    </div>
</main>
<?php require VIEW_PATH . '/partials/footer.php'; ?>
