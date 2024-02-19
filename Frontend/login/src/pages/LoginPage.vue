<template>
  <div class="main">
    <div class="login">
      <div class="description">
        <h1>Авторизация</h1>
        <p>Какой то текст Кавапвакой то текст апвапваКакой то текст Какой то текстыфвфывфыв</p>
      </div>
      <div class="form">
        <form @submit.prevent>
          <label for="email">Email</label>
          <custom-input type="text" id="email" placeholder="example@test.ru"
                        :style="{ boxShadow: badCredential ? '0px 2px 0px 0px rgb(185 97 97)' : 'none' }"
                        v-model="credentials.usernameOrEmail" autocomplete="off"/>
          <label for="password">Password</label>&nbsp;
          <font-awesome-icon v-if="hidePassword" :icon="['fas', 'eye']" @click="hidePassword = !hidePassword"/>
          <font-awesome-icon v-else :icon="['fas', 'eye-slash']" @click="hidePassword = !hidePassword"/>
          <custom-input :type="passwordFieldType" id="password"
                 :style="{ boxShadow: badCredential ? '0px 2px 0px 0px rgb(185 97 97)' : 'none' }"
                 v-model="credentials.password" placeholder="**********"/>
          <button @click="fetchLogin">Log in</button>
        </form>
      </div>
    </div>
  </div>
</template>

<script>
import axios from "axios";
import router from "@/router/router";
import CustomInput from "@/components/UI/CustomInput.vue";

export default {
  components: {CustomInput},
  data() {
    return {
      passwordFieldType: 'password',
      hidePassword: true,
      badCredential: false,
      credentials: {
        usernameOrEmail: '',
        password: ''
      },
    }
  },
  methods: {
    async fetchLogin() {
      try {
        const response = await axios.post('http://localhost:5100/api/v1/login', {
          "usernameOrEmail": this.credentials.usernameOrEmail,
          "password": this.credentials.password
        }, {
          withCredentials: true
        });
        console.log(response.data)
        await router.push('/');
      } catch (e) {
        if (e.response && e.response.status === 401) {
          alert('Ошибка входа: Неверное имя пользователя или пароль.');
          this.badCredential = true;
        } else {
          alert(e);
        }
      } finally {
        this.credentials.password = ''
      }
    },
    checkSession() {
      axios.get('http://localhost:5100/api/v1/user', {withCredentials: true})
          .then(response => {
            console.log(response);
            router.push('/');
          })
          .catch((error) => {
            console.log(error);
            router.push('/login');
          });
    }
  },
  watch: {
    hidePassword(newValue) {
      this.passwordFieldType = newValue ? "password" : "text"
    },
    credentials() {
      console.log(this.credentials)
    }
  },
  mounted() {
    this.checkSession()
  },
}
</script>

<style scoped>
* {
  box-sizing: border-box;
  font-family: Verdana, sans-serif;
}

html,
body {
  height: 100%;
  margin: 0;
  padding: 0;
  width: 100%;
}

div.main {
  width: 100%;
  display: flex;
  align-items: center;
  justify-content: center;
  height: 100vh;
}

div.main div.login {
  align-items: center;
  background-color: #e2e2e5;
  display: flex;
  justify-content: center;
  width: 100%;
  height: 100%;
}

div.main div.login div.description {
  background-color: #ffffff;
  width: 300px;
  padding: 35px;
}

div.main div.login div.description h1,
div.main div.login div.description p {
  margin: 0;
}

div.main div.login div.description p {
  font-size: 0.8em;
  color: #95a5a6;
  margin-top: 10px;
}

div.main div.login div.form {
  background-color: #34495e;
  border-radius: 5px;
  box-shadow: 0px 0px 30px 0px #666;
  color: #ecf0f1;
  width: 260px;
  padding: 35px;
}

div.main div.login div.form label {
  outline: none;
  width: 100%;
}

div.main div.login div.form label {
  color: #95a5a6;
  font-size: 0.8em;
}


div.main div.login div.form ::placeholder {
  color: #768181;
  opacity: 1;
}

div.main div.login div.form button {
  background-color: #ffffff;
  cursor: pointer;
  border: none;
  padding: 10px;
  transition: background-color 0.2s ease-in-out;
  width: 100%;
}

div.main div.login div.form button:hover {
  background-color: #eeeeee;
}

@media screen and (max-width: 600px) {
  div.main div.login {
    align-items: unset;
    background-color: unset;
    display: unset;
    justify-content: unset;
  }

  div.main div.login div.description {
    margin: 0 auto;
    max-width: 350px;
    width: 100%;
  }

  div.main div.login div.form {
    border-radius: unset;
    box-shadow: unset;
    width: 100%;
  }

  div.main div.login div.form form {
    margin: 0 auto;
    max-width: 280px;
    width: 100%;
  }
}
</style>