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
        <template v-slot:slot2>
          <div class="mt-3">
            <custom-card>
              <div class="card-body">
                <div class="row">
                  <div class="col-sm-7">
                    <h6 class="mb-0">Действия</h6>
                  </div>
                </div>
                <hr>
                <div class="row">
                  <div v-if="!currentUser.isLocked" class="col-sm-7">
                    <button class="btn btn-danger" @click="changeBlockUser">Заблокировать</button>
                  </div>
                  <div v-else class="col-sm-7">
                    <button class="btn btn-danger" @click="changeBlockUser">Разблокировать</button>
                  </div>
                </div>
                <hr>
              </div>
            </custom-card>
          </div>
        </template>
      </profile-component>
    </custom-dialog>
    <h1>Users</h1>
    <input v-model="searchQuery" class="form-control mb-1" type="search" placeholder="Поиск пользователей"
           aria-label="Search">
    <table class="table">
      <thead>
      <tr>
        <th scope="col" @click="changeSort('id')">
          # <span v-if="selectedSort === 'id' && sortType === 'asc'">&#9650;</span><span
            v-if="selectedSort === 'id' && sortType === 'desc'">&#9660;</span>
        </th>
        <th scope="col" @click="changeSort('realName')">
          ФИО <span v-if="selectedSort === 'realName' && sortType === 'asc'">&#9650;</span><span
            v-if="selectedSort === 'realName' && sortType === 'desc'">&#9660;</span>
        </th>
        <th scope="col" @click="changeSort('username')">
          Логин <span v-if="selectedSort === 'username' && sortType === 'asc'">&#9650;</span><span
            v-if="selectedSort === 'username' && sortType === 'desc'">&#9660;</span>
        </th>
        <th scope="col" @click="changeSort('email')">
          Email <span v-if="selectedSort === 'email' && sortType === 'asc'">&#9650;</span><span
            v-if="selectedSort === 'email' && sortType === 'desc'">&#9660;</span>
        </th>
        <th scope="col" @click="changeSort('phoneNumber')">
          Телефон <span v-if="selectedSort === 'phoneNumber' && sortType === 'asc'">&#9650;</span><span
            v-if="selectedSort === 'phoneNumber' && sortType === 'desc'">&#9660;</span>
        </th>
        <th scope="col">Роли</th>
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
      selectedSort: 'id',
      sortType: 'asc',
      searchQuery: '',
      page: 0,
      size: 12,
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
            sortColumn: this.selectedSort,
            sortType: this.sortType,
            queryString: this.searchQuery
          },
          withCredentials: true
        });
        this.totalPages = response.data.totalPages;
        this.users = response.data.content;
      } catch (e) {
        this.$refs.notificationRef.showNotification(e.message, 3000, "error");
      }
    },
    async aboutUser(user) {
      try {
        const response = await axios.get('http://localhost:5100/api/v1/admin/users/' + user.id, {
          withCredentials: true
        });
        this.currentUser = response.data;
        this.userDialogVisible = true;
      } catch (e) {
        this.$refs.notificationRef.showNotification(e.message, 3000, "error");
      }
    },
    async changeBlockUser() {
      try {
        let isLock = this.currentUser.isLocked ? "unlock" : "lock";
        await axios.post('http://localhost:5100/api/v1/admin/users/' + this.currentUser.id + "/" + isLock, {}, {
          withCredentials: true
        });
        this.currentUser.isLocked = !this.currentUser.isLocked;
      } catch (e) {
        console.log(e);
        this.$refs.notificationRef.showNotification(e.response.data.message, 3000, "error");
      }
    },
    changeSort(sortColumn) {
      console.log(sortColumn)
      if (sortColumn === this.selectedSort) {
        this.sortType = this.sortType === 'asc' ? 'desc' : 'asc';
        return;
      }
      this.selectedSort = sortColumn;
      this.sortType = 'asc'
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
    searchQuery() {
      this.fetchUsers();
    },
    sortType() {
      this.fetchUsers();
    },
    selectedSort() {
      this.fetchUsers();
    }
  }

}
</script>

<style scoped>

</style>