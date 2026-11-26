<?php require VIEW_PATH . '/partials/header.php'; ?>
<main id="conteudo" class="pagina-cadastro-profissional">
    <div class="cadastro-profissional-container">
        <section class="cadastro-profissional-apresentacao">
            <div>
                <p class="cadastro-profissional-etiqueta">SEU CARINHO PODE TRANSFORMAR ROTINAS</p>
                <h1>Compartilhe seu jeito de cuidar.</h1>
                <p>Apresente seu trabalho a tutores que procuram um profissional atencioso e de confiança.</p>
                <ul><li><span>✓</span> Mostre seus serviços e experiência</li><li><span>✓</span> Combine os cuidados com cada família</li></ul>
            </div>
            <div class="cadastro-profissional-arte" aria-hidden="true">
                <img class="cadastro-profissional-pet" src="assets/img/composicao-alegria.webp" alt="Cachorro alegre">
            </div>
            <p class="cadastro-profissional-nota">Cuidado próximo, transparência e respeito a cada pet.</p>
        </section>

        <section class="cadastro-profissional-form-card" aria-labelledby="titulo-cadastro-profissional">
            <div class="cadastro-profissional-cabecalho">
                <a class="cadastro-profissional-voltar" href="tipo.php">← Voltar à escolha de perfil</a>
                <p class="cadastro-profissional-etiqueta">CADASTRO PROFISSIONAL</p>
                <h2 id="titulo-cadastro-profissional">Conte sobre seu trabalho</h2>
                <p>Vamos criar seu perfil para os tutores conhecerem você.</p>
            </div>
            <form class="cadastro-profissional-formulario" method="post" action="profissional.php">
                <input type="hidden" name="acao" value="cadastro_profissional">
                <?= csrfCampo() ?>
                <label class="campo">Nome completo
                    <input type="text" name="nome" placeholder="Seu nome e sobrenome" autocomplete="name">
                </label>
                <div class="cadastro-profissional-duplo">
                    <label class="campo">E-mail
                        <input type="email" name="email" placeholder="voce@exemplo.com" autocomplete="email">
                    </label>
                    <label class="campo">Celular
                        <input type="tel" name="telefone" placeholder="(11) 99999-9999" autocomplete="tel">
                    </label>
                </div>
                <div class="cadastro-profissional-duplo">
                    <label class="campo">Cidade ou região
                        <input type="text" name="regiao" placeholder="Ex.: São Paulo, SP" autocomplete="address-level2">
                    </label>
                    <label class="campo">Principal serviço
                        <select name="servico"><option value="">Escolha um serviço</option><option>Pet sitting</option><option>Passeios</option><option>Hospedagem</option><option>Creche</option></select>
                    </label>
                </div>
                <label class="campo">Uma breve apresentação
                    <textarea name="apresentacao" rows="3" placeholder="Conte um pouco sobre sua experiência com pets"></textarea>
                </label>
                <div class="cadastro-profissional-duplo">
                    <label class="campo">Senha
                        <input type="password" name="senha" placeholder="Crie uma senha" autocomplete="new-password">
                    </label>
                    <label class="campo">Confirmar senha
                        <input type="password" name="confirmar-senha" placeholder="Repita sua senha" autocomplete="new-password">
                    </label>
                </div>
                <label class="opcao-checkbox cadastro-profissional-termos"><input type="checkbox" name="termos"> Li e aceito os <a href="#termos">termos de uso</a> e a política de privacidade.</label>
                <button class="botao botao-primario botao-cheio" type="submit">Criar perfil profissional</button>
            </form>
            <p class="cadastro-profissional-entrar">Já tem uma conta? <a href="login.php">Entrar</a></p>
        </section>
    </div>
</main>
<?php require VIEW_PATH . '/partials/footer.php'; ?>
