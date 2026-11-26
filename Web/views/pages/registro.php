<?php require VIEW_PATH . '/partials/header.php'; ?>
<main id="conteudo" class="pagina-novo-registro">
    <div class="novo-registro-container">
        <div class="novo-registro-cabecalho">
            <div><p class="novo-registro-etiqueta">DIÁRIO DO PET</p><h1>Novo registro</h1></div>
            <a href="diario.php">← Voltar ao diário</a>
        </div>

        <div class="novo-registro-layout">
            <form class="novo-registro-formulario" method="post" action="registro.php">
                <input type="hidden" name="acao" value="novo_registro">
                <?= csrfCampo() ?>
                <div class="novo-registro-campos-duplos">
                    <label class="novo-registro-campo">Pet<select name="pet_id" required><?php foreach ($petsRegistro as $pet): ?><option value="<?= (int) $pet['id'] ?>"><?= escapar($pet['nome']) ?> · <?= escapar($pet['raca']) ?></option><?php endforeach; ?></select></label>
                    <label class="novo-registro-campo">Categoria<select name="categoria"><option>Bem-estar</option><option>Alimentação</option><option>Água</option><option>Passeio</option><option>Saúde</option></select></label>
                    <label class="novo-registro-campo">Data<input type="date" name="data" value="2026-10-01"></label>
                    <label class="novo-registro-campo">Horário<input type="time" name="horario" value="16:30"></label>
                </div>
                <label class="novo-registro-campo">Título<input type="text" name="titulo" placeholder="Ex.: Passeio da manhã" required></label>
                <label class="novo-registro-campo">Como foi?<textarea name="relato" rows="4" maxlength="1000" placeholder="Conte como foi esse momento com seu pet." required></textarea><small>Até 1.000 caracteres</small></label>
                <label class="novo-registro-foto" for="foto-registro"><?= iconeSvg('camera') ?><strong>Adicionar uma foto</strong><small>Opcional · Escolha um momento do seu pet.</small><input id="foto-registro" type="file" accept="image/*" aria-label="Adicionar uma foto ao registro"></label>
                <div class="novo-registro-acoes"><button class="botao botao-primario" type="submit">Salvar registro</button><a href="diario.php">Cancelar</a></div>
            </form>

            <aside class="novo-registro-lateral">
                <section class="novo-registro-dica">
                    <div class="novo-registro-arte"><img src="assets/img/composicao-curiosidade.webp" alt="Gato curioso acompanhando o registro"></div>
                    <h2>Uma atualização faz toda a diferença.</h2>
                    <p>Conte o que aconteceu de forma simples. Descreva o comportamento do Thor e os cuidados realizados.</p>
                </section>
                <section class="novo-registro-autor"><span aria-hidden="true"><?= escapar(substr($usuarioRegistro['nome'], 0, 2)) ?></span><div><strong><?= escapar($usuarioRegistro['nome']) ?></strong><small>Profissional · Autora do registro</small><p>O tutor do pet também poderá ver este registro.</p></div></section>
            </aside>
        </div>
    </div>
</main>
<?php require VIEW_PATH . '/partials/footer.php'; ?>
