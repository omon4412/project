<template>
  <notification-window ref="notificationRef"></notification-window>
  <profile-component :email="email" :fullName="fullName" :login="login" :phone="phone" :roles="roles"></profile-component>
</template>

<script>
import axios from "axios";
import NotificationWindow from "@/components/UI/NotificationWindow.vue";
import checkSession from "@/pages/check";
import router from "@/router/router";
import ProfileComponent from "@/components/ProfileComponent.vue";

export default {
  components: {ProfileComponent, NotificationWindow},
  data() {
    return {
      fullName: '',
      login: '',
      email: '',
      phone: '',
      roles: [],
    }
  },
  methods: {
    async fetchUserData() {
      try {
        const response = await axios.get('http://localhost:5100/api/v1/user', {
          withCredentials: true
        });
        this.fullName = response.data.realName;
        this.login = response.data.username;
        this.email = response.data.email;
        this.phone = response.data.phoneNumber;
        this.roles = response.data.roles;
      } catch (e) {
        this.badCredential = true;
        if (e.response && e.response.status === 401) {
          this.$refs.notificationRef.showNotification("Неверный логин или пароль", 3000, "error");
        } else if (e.response && e.response.status === 500) {
          this.$refs.notificationRef.showNotification("Сервер авторизации недоступен", 3000, "error");
        } else {
          this.$refs.notificationRef.showNotification(e.message, 3000, "error");
        }
      }
    },
  },
  async mounted() {
    const session = await checkSession();
    console.log(session + " mainpage " + "mounted")
    if (session === '') {
      await router.push('/login');
      return
    }
    await this.fetchUserData();
  },
}
</script>

<style scoped>

</style>