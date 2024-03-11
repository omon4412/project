<template>
  <notification-window ref="notificationRef"></notification-window>
  <profile-component :email="email" :fullName="fullName" :login="login" :phone="phone" :roles="roles">
    <template v-slot:slot1>
      <nav aria-label="breadcrumb" class="main-breadcrumb">
        <ol class="breadcrumb">
          <li class="breadcrumb-item">
            <router-link to="/">Home</router-link>
          </li>
          <li class="breadcrumb-item active" aria-current="page">User Profile</li>
        </ol>
      </nav>
    </template>
    <template v-slot:slot2>
      <div class="mt-3">
        <custom-card>
          <div class="card-body">
            <div class="row">
              <div class="col-sm-3">
                <h6 class="mb-0">IP</h6>
              </div>
              <div class="col-sm-7">
                <h6 class="mb-0">User Agent</h6>
              </div>
              <div class="col-sm-2">
                <h6 class="mb-0">Действие</h6>
              </div>
            </div>
            <hr>
            <div class="row" v-for="session in sessions" :key="session.sessionId">
              <div class="col-sm-3 text-secondary">
                <h6 class="mb-0">{{ session.sessionDetails.remoteAddress }}</h6>
              </div>
              <div class="col-sm-7 text-secondary">
                {{ session.sessionDetails.userAgent }}
              </div>
              <div class="col-sm-2">
                <button @click="terminateSession(session.sessionId)" class="btn btn-danger">Выйти</button>
              </div>
              <hr>
            </div>
          </div>
        </custom-card>
      </div>
    </template>
  </profile-component>
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
      sessions: []
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
        this.$refs.notificationRef.showNotification(e.message, 3000, "error");
      }
    },
    async fetchSessionsData() {
      try {
        const response = await axios.get('http://localhost:5100/api/v1/user/sessions', {
          withCredentials: true
        });
        this.sessions = response.data;
        console.log(this.sessions)
      } catch (e) {
        this.$refs.notificationRef.showNotification(e.message, 3000, "error");
      }
    },
    async terminateSession(sessionId) {
      try {
        await axios.delete('http://localhost:5100/api/v1/user/sessions/' + sessionId, {
          withCredentials: true
        });
        this.sessions = this.sessions.filter(s => s.sessionId !== sessionId);
      } catch (e) {
        this.$refs.notificationRef.showNotification(e.message, 3000, "error");
      }
    }
  },
  async mounted() {
    const session = await checkSession();
    console.log(session + " mainpage " + "mounted")
    if (session === '') {
      await router.push('/login');
      return
    }
    await this.fetchUserData();
    await this.fetchSessionsData();
  },
}
</script>

<style scoped>

</style>