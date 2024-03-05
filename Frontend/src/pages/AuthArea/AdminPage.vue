<template>
  <div id="wrapper" class="wrapper-content" :class="showSideBar?'':'toggled'">
    <div id="sidebar-wrapper">
      <ul class="sidebar-nav">
        <br>
        <li v-for="(item, index) in items" :key="index" :class="{ 'active': selectedItem === index }"
            @click="selectItem(index)">
          <router-link :to="{ path: '/admin' + item.url }">{{ item.name }}</router-link>
        </li>
      </ul>
    </div>

    <div id="page-content-wrapper" style="position: relative;">
      <div class="container-fluid">
        <div>
          <div style="position: absolute; top: 10px; left: 10px;">
            <custom-button class="toggle-button" @click="showSideBarFunc" type="button">
              <font-awesome-icon :icon="['fas', 'navicon']"/>
            </custom-button>
          </div>
          <div class="col-lg-12">
            <h1>Admin Dashboard</h1>
            <hr>
            <router-view></router-view>
          </div>
        </div>
      </div>
    </div>
  </div>
</template>

<script>
import checkSession from "@/pages/check";
import router from "@/router/router";
import CustomButton from "@/components/UI/CustomButton.vue";
import store from "@/store";

export default {
  components: {CustomButton},
  data() {
    return {
      showSideBar: true,
      items: [
        {name: "User", url: "/users"},],
        //{name: "Shortcuts", url: "/test2"}
      selectedItem: null
    }
  },
  methods: {
    showSideBarFunc() {
      this.showSideBar = !this.showSideBar;
    },
    selectItem(index) {
      store.commit('updateAdminOpenPage', index);
      this.selectedItem = index;
    }
  },
  async mounted() {
    const user = await checkSession();
    console.log(user + " adminpage " + "mounted")
    if (user === '') {
      await router.push('/login');
    } else if (!user.roles.some(role => role.name === "ROLE_ADMIN")) {
      await router.push('/403');
    }
    this.selectItem(this.$store.getters.getAdminOpenPage);
  },
}
</script>
<style scoped>

body {
  background: #f4f3ef;
}

.wrapper-content {
  height: 100%;
}

#wrapper {
  padding-left: 0;
  -webkit-transition: all 0.5s ease;
  -moz-transition: all 0.5s ease;
  -o-transition: all 0.5s ease;
  transition: all 0.5s ease;
  min-height: calc(100vh - 113px);
}

#wrapper.toggled {
  padding-left: 250px;
}

#sidebar-wrapper {
  position: absolute;
  left: 250px;
  width: 0;
  height: calc(100vh - 113px);
  margin-left: -250px;
  overflow-y: auto;
  background: #fff;
  -webkit-transition: all 0.5s ease;
  -moz-transition: all 0.5s ease;
  -o-transition: all 0.5s ease;
  transition: all 0.5s ease;
}

#sidebar-wrapper {
  box-shadow: inset -1px 0px 0px 0px #DDDDDD;
}

#wrapper.toggled #sidebar-wrapper {
  width: 250px;
}

#page-content-wrapper {
  width: 100%;
  position: absolute;
  padding: 15px;
}

#wrapper.toggled #page-content-wrapper {
  position: absolute;
  margin-right: -250px;
}

/* Sidebar Styles */

.sidebar-nav {
  position: absolute;
  top: 0;
  width: 250px;
  margin: 0;
  padding: 0;
  list-style: none;
}

.sidebar-nav li {
  text-indent: 20px;
  line-height: 40px;
}

.sidebar-nav li a {
  display: block;
  text-decoration: none;
  color: #999999;
}

.sidebar-nav li a:hover {
  text-decoration: none;
}

.sidebar-nav li a:active,
.sidebar-nav li a:focus {
  text-decoration: none;
}

.sidebar-nav > .sidebar-brand {
  height: 65px;
  font-size: 18px;
  line-height: 60px;
}

.sidebar-nav > .sidebar-brand a {
  color: #999999;
}

.sidebar-nav > .sidebar-brand a:hover {
  color: #fff;
  background: none;
}

@media (min-width: 768px) {
  #wrapper {
    padding-left: 250px;
  }

  #wrapper.toggled {
    padding-left: 0;
  }

  #sidebar-wrapper {
    width: 251px;
  }

  #wrapper.toggled #sidebar-wrapper {
    width: 0;
  }

  #page-content-wrapper {
    padding: 20px;
    position: relative;
  }

  #wrapper.toggled #page-content-wrapper {
    position: relative;
    margin-right: 0;
  }
}

#sidebar-wrapper li.active > a:after {
  border-right: 17px solid #f4f3ef;
  border-top: 17px solid transparent;
  border-bottom: 17px solid transparent;
  content: "";
  display: inline-block;
  position: absolute;
  right: -1px;
}

.sidebar-brand {
  border-bottom: 1px solid rgba(102, 97, 91, 0.3);
}

.sidebar-brand {
  padding: 18px 0px;
  margin: 0 20px;
}

.navbar .navbar-nav > li > a p {
  display: inline-block;
  margin: 0;
}

p {
  font-size: 16px;
  line-height: 1.4em;
}

.navbar-default {
  background-color: #f4f3ef;
  border: 0px;
  border-bottom: 1px solid #DDDDDD;
}

btn-menu {
  border-radius: 3px;
  padding: 4px 12px;
  margin: 14px 5px 5px 20px;
  font-size: 14px;
  float: left;
}

.active {
  background-color: #ccc;
}

.toggle-button {
  padding: 0;
}
</style>