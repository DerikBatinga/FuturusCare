<?php require VIEW_PATH . '/partials/header.php'; ?>
<main id="conteudo" class="pagina-cadastro-tutor">
    <div class="cadastro-tutor-container">
        <section class="cadastro-tutor-apresentacao">
            <div>
                <p class="cadastro-tutor-etiqueta">UM CUIDADO PARA CADA MOMENTO</p>
                <h1>Mais tranquilidade para você e seu pet.</h1>
                <p>Crie sua conta e encontre profissionais que respeitam a rotina e o jeitinho do seu companheiro.</p>
            </div>
            <div class="cadastro-tutor-arte" aria-hidden="true">
                <img class="cadastro-tutor-pet" src="assets/img/composicao-amizade.webp" alt="Cão e gato juntos">
            </div>
            <p class="cadastro-tutor-nota"><?= iconeSvg('heart') ?> Um espaço seguro para cuidar de quem faz parte da família.</p>
        </section>

        <section class="cadastro-tutor-form-card" aria-labelledby="titulo-cadastro-tutor">
            <div class="cadastro-tutor-cabecalho">
                <a class="cadastro-tutor-voltar" href="tipo.php">← Voltar à escolha de perfil</a>
                <p class="cadastro-tutor-etiqueta">CADASTRO DE TUTOR</p>
                <h2 id="titulo-cadastro-tutor">Crie sua conta</h2>
                <p>Preencha seus dados para começar.</p>
            </div>
            <form class="cadastro-tutor-formulario" method="post" action="tutor.php">
                <input type="hidden" name="acao" value="cadastro_tutor">
                <?= csrfCampo() ?>
                <label class="campo">Nome completo
                    <input type="text" name="nome" placeholder="Como podemos chamar você?" autocomplete="name">
                </label>
                <div class="cadastro-tutor-duplo">
                    <label class="campo">E-mail
                        <input type="email" name="email" placeholder="voce@exemplo.com" autocomplete="email">
                    </label>
                    <label class="campo">Celular
                        <input type="tel" name="telefone" placeholder="(11) 99999-9999" autocomplete="tel">
                    </label>
                </div>
                <div class="cadastro-tutor-duplo">
                    <label class="campo">Senha
                        <input type="password" name="senha" placeholder="Crie uma senha" autocomplete="new-password">
                    </label>
                    <label class="campo">Confirmar senha
                        <input type="password" name="confirmar-senha" placeholder="Repita sua senha" autocomplete="new-password">
                    </label>
                </div>
                <label class="opcao-checkbox cadastro-tutor-termos"><input type="checkbox" name="termos"> Li e aceito os <a href="#termos">termos de uso</a> e a política de privacidade.</label>
                <button class="botao botao-primario botao-cheio" type="submit">Criar minha conta</button>
            </form>
            <p class="cadastro-tutor-entrar">Já tem uma conta? <a href="login.php">Entrar</a></p>
        </section>
    </div>
</main>
<?php require VIEW_PATH . '/partials/footer.php'; ?>
