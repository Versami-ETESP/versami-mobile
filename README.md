# :iphone: VERSAMI MOBILE

Esta aplicação é um dos componentes do Trabalho de Conclusão de Curso (TCC) técnico da **ETEC de São Paulo**, desenvolvida pelos alunos **Julia Belchior**, **Matheus Canesso**, **Thamiris Fernandes** e **Ygor Silva**.

## :warning: Atenção
Antes de iniciar a configuração do aplicativo mobile, é **obrigatório executar todos os scripts de banco de dados** que estão disponíveis no repositório [`versami-documentacao`](https://github.com/Versami-ETESP/versami-documentacao)

## ☑️ Pré-requisitos

* Android Studio instalado na máquina.
* Git instalado (opcional, mas recomendado).
* Banco de dados SQL Server com os scripts do projeto executados.

## :gear: Como configurar o projeto

### 1. Clonar o repositório

Se você **possui o Git instalado**, abra o terminal na pasta desejada e execute:

```bash
git clone https://github.com/Versami-ETESP/versami-mobile.git
```

Caso **não possua o Git**, acesse a página do repositório, clique no botão `Code` e depois em `Download ZIP`.

![image](https://github.com/user-attachments/assets/16fea5ed-b497-4368-bb49-43070a74cf43)

Depois de baixado, **extraia o arquivo ZIP** para uma pasta de sua escolha.

---
### 2. Abrir o projeto no Android Studio

* Abra o **Android Studio**
* Clique em **"Open"**
* Navegue até a pasta onde o projeto foi extraído/clonado
* Expanda a estrutura de diretórios e procure pelo arquivo `build.gradle` (nível do projeto)
* Dê dois cliques no arquivo `build.gradle` e aguarde o Android Studio sincronizar as dependências

![image](https://github.com/user-attachments/assets/8f1e056c-4908-4308-aa0c-534191d52a0b)

---

### 3. Configurar a conexão com o banco de dados

* No painel esquerdo, vá até `java > com.example.versami.util`
* Abra a classe `Conexao.java`

![image](https://github.com/user-attachments/assets/fee2fd6f-99e0-49c3-b834-1222076e6c85)

* **Na linha 31**, altere o endereço IP da string de conexão para o IP da **máquina que possui o SQL Server com o banco da aplicação**.
* **Na linha 32**, configure o **usuário e a senha** do seu SQL Server:

![image](https://github.com/user-attachments/assets/7577c2d0-f78f-4f39-aa6e-4e5b7eb671e5)

```java
String url = "jdbc:jtds:sqlserver://<SEU_IP>:1433/versami;loginTimeout=5;socketTimeout=5";
connect = DriverManager.getConnection(url,"<SEU_USUARIO>","<SUA_SENHA>");
```

---

### :white_check_mark: Pronto!

Com tudo configurado, você já pode **executar a aplicação no emulador do Android Studio** clicando no botão "Run".

## :iphone: Como gerar o APK para instalar no celular

Se quiser testar o app diretamente em um **dispositivo Android físico**, siga os passos:

1. Com o projeto aberto, clique no menu **Build**
2. Vá em **Build Bundle(s) / APK(s) > Build APK(s)**

![image](https://github.com/user-attachments/assets/27e5eb67-9834-4b5f-af36-fe9bd932a3e1)

3. Após o processo, aparecerá uma notificação no canto inferior direito. Clique em **"Locate"** para abrir a pasta onde o APK foi gerado.

![image](https://github.com/user-attachments/assets/32644b2b-28e2-42f7-a1f0-3c511dffb261)

4. **Copie o arquivo APK** para o seu celular via cabo USB ou outra forma de transferência.

5. No dispositivo Android:

   * Ative a opção **"Instalar apps de fontes desconhecidas"** (varia conforme o modelo).
   * Abra o arquivo APK e conclua a instalação.
