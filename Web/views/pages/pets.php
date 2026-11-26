<?php require VIEW_PATH . '/partials/header.php'; ?>
<main id="conteudo" class="pagina-cadastro-pet">
    <div class="cadastro-pet-container">
        <a class="cadastro-pet-voltar" href="tutorpainel.php">← Voltar ao meu perfil</a>
        <section class="cadastro-pet-formulario-card" aria-labelledby="titulo-cadastro-pet">
            <p class="cadastro-pet-etiqueta">PERFIL DO TUTOR · MEUS PETS</p>
            <h1 id="titulo-cadastro-pet"><?= $petEditando ? 'Editar cadastro do pet' : 'Cadastrar novo pet' ?></h1>
            <form class="cadastro-pet-formulario" method="post" action="pets.php" enctype="multipart/form-data">
                <input type="hidden" name="acao" value="<?= escapar($acaoPet) ?>">
                <?= csrfCampo() ?>
                <?php if ($petEditando): ?><input type="hidden" name="pet_id" value="<?= (int) $petEditando['id'] ?>"><?php endif; ?>
                <label class="cadastro-pet-foto" for="foto-pet">
                    <?php if (!empty($petEditando['foto'])): ?><img src="<?= escapar($petEditando['foto']) ?>" alt="Foto atual de <?= escapar($petEditando['nome']) ?>"><?php endif; ?>
                    <svg aria-hidden="true" viewBox="0 0 24 24"><path d="M4 7.5h3l1.4-2h7.2l1.4 2h3A1.5 1.5 0 0 1 21.5 9v9a1.5 1.5 0 0 1-1.5 1.5H4A1.5 1.5 0 0 1 2.5 18V9A1.5 1.5 0 0 1 4 7.5Z"/><circle cx="12" cy="13" r="3.5"/></svg>
                    <strong><?= !empty($petEditando['foto']) ? 'Trocar foto do pet' : 'Adicionar foto do pet' ?></strong>
                    <input id="foto-pet" name="foto" type="file" accept="image/jpeg,image/png,image/webp" aria-label="Adicionar foto do pet">
                </label>

                <label class="cadastro-pet-campo cadastro-pet-nome">Nome do pet<input type="text" name="nome" value="<?= escapar($petEditando['nome'] ?? '') ?>" placeholder="ex: Thor" required></label>
                <div class="cadastro-pet-campos-duplos">
                    <label class="cadastro-pet-campo">Espécie<select name="especie"><option value="cao" <?= ($petEditando['especie'] ?? 'cao') === 'cao' ? 'selected' : '' ?>>Cão</option><option value="gato" <?= ($petEditando['especie'] ?? '') === 'gato' ? 'selected' : '' ?>>Gato</option><option value="outro" <?= ($petEditando['especie'] ?? '') === 'outro' ? 'selected' : '' ?>>Outro</option></select></label>
                    <label class="cadastro-pet-campo">Raça<input type="text" name="raca" value="<?= escapar($petEditando['raca'] ?? '') ?>"></label>
                    <label class="cadastro-pet-campo">Idade (anos)<input type="number" name="idade" value="<?= escapar($petEditando['idade'] ?? '') ?>" min="0" step="1" placeholder="ex: 2" required></label>
                    <label class="cadastro-pet-campo">Peso (kg)<input type="number" name="peso" value="<?= escapar($petEditando['peso'] ?? '') ?>" min="0.1" step="0.1" placeholder="ex: 12,5" required></label>
                </div>
                <label class="cadastro-pet-campo cadastro-pet-observacoes">Observações especiais (alergias / remédios)<textarea name="observacoes" rows="3" placeholder="ex: Alérgico a corantes vermelhos. Toma remédio de ouvido."><?= escapar($petEditando['observacoes'] ?? '') ?></textarea></label>
                <button class="botao botao-primario cadastro-pet-enviar" type="submit"><?= $petEditando ? 'Salvar alterações' : 'Cadastrar pet' ?></button>
            </form>
        </section>
    </div>
</main>
<?php require VIEW_PATH . '/partials/footer.php'; ?>
