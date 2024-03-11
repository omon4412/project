<template>
  <notification-window ref="notificationRef"></notification-window>
  <div>
    <custom-dialog v-model:show="userDialogVisible" v-model:route="routeUser">
      <profile-component style="height: 100% !important;"
                         :email="currentUser.email" :fullName="currentUser.realName" :login="currentUser.username"
                         :phone="currentUser.phoneNumber" :roles="currentUser.roles">
        <template v-slot:slot1>
          <h2>Карточка пользователя - {{ currentUser.id }}</h2>
        </template>
      </profile-component>
    </custom-dialog>
    <h1>Users</h1>
    <table class="table">
      <thead>
      <tr>
        <th scope="col">#</th>
        <th scope="col">ФИО</th>
        <th scope="col">Логин</th>
        <th scope="col">Email</th>
        <th scope="col">Телефон</th>
        <th scope="col">Роли</th>
        <th scope="col"></th>
      </tr>
      </thead>
      <tbody>
      <tr v-for="user in users" :key="user.id" @click="aboutUser(user)">
        <td>{{ user.id }}</td>
        <td>{{ user.realName }}</td>
        <td>{{ user.username }}</td>
        <td>{{ user.email }}</td>
        <td>{{ user.phoneNumber }}</td>
        <td>{{ user.roles }}</td>
        <!--        <router-link :to="{ path: '/admin/users/' + user.id }">Профиль</router-link>-->
      </tr>
      </tbody>
    </table>
    <page-wrapper v-bind:page="page" v-bind:totalPages="totalPages" v-model:page="page"/>
  </div>
</template>

<script>
import axios from "axios";
import ProfileComponent from "@/components/ProfileComponent.vue";

export default {
  components: {ProfileComponent},
  data() {
    return {
      users: [],
      selectedSort: '',
      searchQuery: '',
      page: 0,
      size: 10,
      totalPages: 0,
      userDialogVisible: false,
      currentUser: null,
      routeUser: '',
    }
  },
  methods: {
    async fetchUsers() {
      try {
        const response = await axios.get('http://localhost:5100/api/v1/admin/users', {
          params: {
            size: this.size,
            from: this.page,
            //sort: this.selectedSort,
            //title_like: this.searchQuery
          },
          withCredentials: true
        });
        //console.log(response.data)
        this.totalPages = response.data.totalPages;
        this.users = response.data.content;
        console.log(this.users)
      } catch (e) {
        this.$refs.notificationRef.showNotification(e.message, 3000, "error");
      }
    },
    aboutUser(user) {
      console.log(user)
      this.currentUser = user;
      this.userDialogVisible = true;
    }
  },
  async mounted() {
    await this.fetchUsers();
    //console.log(this.users)
  },
  watch: {
    page() {
      this.fetchUsers();
    },
  }

}
</script>

<style scoped>

</style>