<?php require VIEW_PATH . '/partials/header.php'; ?>
<main id="conteudo" class="pagina-agendamento">
    <div class="booking-container">
        <section class="booking-busca" aria-labelledby="titulo-booking">
            <h1 id="titulo-booking">Quem vai acompanhar <?= escapar($petBooking['nome'] ?? 'seu pet') ?>?</h1>
            <form class="booking-filtros" method="post" action="servico.php">
                <input type="hidden" name="acao" value="escolher_servico"><?= csrfCampo() ?>
                <label>Serviço
                    <select name="servico"><option <?= $servicoBooking === 'Passeio com cães' ? 'selected' : '' ?>>Passeio com cães</option><option <?= $servicoBooking === 'Pet sitter' ? 'selected' : '' ?>>Pet sitter</option><option <?= $servicoBooking === 'Hospedagem' ? 'selected' : '' ?>>Hospedagem</option></select>
                </label>
                <label>Local
                    <input type="text" name="local" value="<?= escapar($localBooking) ?>" aria-label="Local do atendimento">
                </label>
                <label>Data
                    <input type="text" name="data" value="Escolher data" aria-label="Data do atendimento" readonly>
                </label>
                <label>Pet
                    <select name="pet_id" aria-label="Pet"><?php foreach (usuariosDoTutor((int) $usuarioTutor['id']) as $pet): ?><option value="<?= (int) $pet['id'] ?>" <?= (int) $pet['id'] === (int) ($rascunhoBooking['pet_id'] ?? 0) ? 'selected' : '' ?>><?= escapar($pet['nome']) ?></option><?php endforeach; ?></select>
                </label>
                <button class="botao botao-primario" type="submit">Buscar cuidado</button>
            </form>
        </section>

        <nav class="booking-etapas" aria-label="Etapas do agendamento">
            <span class="etapa-concluida"><b>✓</b> Serviço</span>
            <span class="etapa-atual"><b>2</b> Profissional</span>
            <span><b>3</b> Data e horário</span>
            <span><b>4</b> Revisão</span>
        </nav>

        <div class="booking-profissionais-layout">
            <aside class="booking-filtros-profissionais" aria-labelledby="titulo-filtros-profissionais">
                <div class="booking-filtro-titulo"><h2 id="titulo-filtros-profissionais">Filtrar profissionais</h2><a href="cuidador.php">Limpar filtros</a></div>
                <fieldset>
                    <legend>Tipo de pet</legend>
                    <label><input type="checkbox" checked> Cachorro</label>
                    <label><input type="checkbox"> Gato</label>
                </fieldset>
                <fieldset>
                    <legend>Porte do pet</legend>
                    <label><input type="checkbox" checked> Todos os portes</label>
                    <label><input type="checkbox"> Pequeno · até 10 kg</label>
                    <label><input type="checkbox"> Médio · 10 a 25 kg</label>
                    <label><input type="checkbox"> Grande · acima de 25 kg</label>
                </fieldset>
                <fieldset class="booking-filtro-preco">
                    <legend>Valor por passeio</legend>
                    <strong>Até R$ 80,00</strong>
                    <input type="range" min="10" max="150" value="80" aria-label="Valor máximo por passeio">
                    <div><span>R$ 10</span><span>R$ 150</span></div>
                </fieldset>
                <fieldset>
                    <legend>Experiência e cuidados</legend>
                    <label><input type="checkbox"> Cuida de pets idosos</label>
                    <label><input type="checkbox"> Administração de medicação</label>
                    <label><input type="checkbox"> Primeiros socorros</label>
                </fieldset>
                <fieldset>
                    <legend>Disponibilidade</legend>
                    <label><input type="checkbox" checked> Atende na minha região</label>
                </fieldset>
            </aside>

            <section class="booking-resultados" aria-labelledby="titulo-resultados-profissionais">
                <h2 id="titulo-resultados-profissionais"><?= count($profissionais) ?> profissionais para cuidar de <?= escapar($petBooking['nome'] ?? 'seu pet') ?></h2>
                <form method="post" action="cuidador.php" class="booking-lista-profissionais-form">
                    <input type="hidden" name="acao" value="selecionar_profissional"><?= csrfCampo() ?>
                <div class="booking-lista-profissionais">
                    <?php foreach ($profissionais as $profissional): ?>
                        <article class="booking-profissional <?= $profissional['selecionada'] ? 'booking-profissional-selecionada' : '' ?>">
                            <span class="booking-profissional-avatar"><?php if (!empty($profissional['foto'])): ?><img src="<?= escapar($profissional['foto']) ?>" alt="Foto de <?= escapar($profissional['nome']) ?>"><?php else: ?><?= escapar($profissional['iniciais']) ?><?php endif; ?></span>
                            <div class="booking-profissional-info">
                                <h3><?= htmlspecialchars($profissional['nome'], ENT_QUOTES, 'UTF-8') ?></h3>
                                <p class="booking-profissional-nota"><span aria-label="5 estrelas">★</span> <?= htmlspecialchars($profissional['nota'], ENT_QUOTES, 'UTF-8') ?> · <?= htmlspecialchars($profissional['avaliacoes'], ENT_QUOTES, 'UTF-8') ?></p>
                                <p><?= htmlspecialchars($profissional['descricao'], ENT_QUOTES, 'UTF-8') ?></p>
                                <small><?= htmlspecialchars($profissional['experiencia'], ENT_QUOTES, 'UTF-8') ?></small>
                            </div>
                            <div class="booking-profissional-acoes">
                                <strong><?= htmlspecialchars($profissional['preco'], ENT_QUOTES, 'UTF-8') ?></strong>
                                <small><?= $profissional['selecionada'] ? 'Selecionada' : 'Atende na região' ?></small>
                                <button class="botao botao-primario" type="submit" name="profissional_id" value="<?= (int) $profissional['id'] ?>">Selecionar profissional</button>
                            </div>
                        </article>
                    <?php endforeach; ?>
                </div>
                </form>
                <a class="booking-voltar-servicos" href="servico.php">Voltar para serviços</a>
            </section>
        </div>
    </div>
</main>
<?php require VIEW_PATH . '/partials/footer.php'; ?>
