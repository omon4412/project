<template>
  <notification-window ref="notificationRef"></notification-window>
  <div>
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
      </tr>
      </thead>
      <tbody>
      <tr v-for="user in users" :key="user.id">
        <td>{{ user.id }}</td>
        <td>{{ user.realName }}</td>
        <td>{{ user.username }}</td>
        <td>{{ user.email }}</td>
        <td>{{ user.phoneNumber }}</td>
        <td>{{ user.roles }}</td>
      </tr>
      </tbody>
    </table>
    <page-wrapper v-bind:page="page" v-bind:totalPages="totalPages" v-model:page="page"/>
  </div>
</template>

<script>
import axios from "axios";

export default {
  data() {
    return {
      users: [],
      selectedSort: '',
      searchQuery: '',
      page: 0,
      size: 10,
      totalPages: 0,
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