<template>
  <nav class="navbar navbar-expand-lg navbar-dark bg-dark">
    <div class="container-fluid">
      <router-link class="navbar-brand" to="/">Navbar</router-link>
      <button class="navbar-toggler" type="button" data-bs-toggle="collapse" data-bs-target="#navbarSupportedContent"
              aria-controls="navbarSupportedContent" aria-expanded="false" aria-label="Toggle navigation">
        <span class="navbar-toggler-icon"></span>
      </button>
      <div class="collapse navbar-collapse" id="navbarSupportedContent">
        <ul class="navbar-nav me-auto mb-2 mb-lg-0">
          <li class="nav-item">
            <router-link class="nav-link active" aria-current="page" to="/">Home</router-link>
          </li>
          <li class="nav-item">
            <router-link class="nav-link" to="/login">Link</router-link>
          </li>
          <li class="nav-item dropdown">
            <a class="nav-link dropdown-toggle" href="#" id="navbarDropdown" role="button" data-bs-toggle="dropdown"
               aria-expanded="false">
              Dropdown
            </a>
            <ul class="dropdown-menu" aria-labelledby="navbarDropdown">
              <li><a class="dropdown-item" href="#">Action</a></li>
              <li><a class="dropdown-item" href="#">Another action</a></li>
              <li>
                <hr class="dropdown-divider">
              </li>
              <li><a class="dropdown-item" href="#">Something else here</a></li>
            </ul>
          </li>
          <li class="nav-item">
            <router-link class="nav-link disabled" href="#" tabindex="-1" aria-disabled="true" to="/">Disabled
            </router-link>
          </li>

        </ul>
        <form class="d-flex">
          <input class="form-control me-2" type="search" placeholder="Search" aria-label="Search">
          <button class="btn btn-outline-success" type="submit">Search</button>
        </form>
        <ul v-if="newuser!==null" class="navbar-nav mb-2 mb-lg-0">
          <li class="nav-item dropdown">
            <a class="nav-link dropdown-toggle" id="navbarDropdown" role="button"
               data-bs-toggle="dropdown"
               aria-expanded="false">
              {{ newuser.username }}
            </a>
            <ul class="dropdown-menu dropdown-menu-end" aria-labelledby="navbarDropdown">
              <li>
                <router-link to="/profile" class="dropdown-item">Profile</router-link>
              </li>
              <li v-if="newuser.roles && newuser.roles.some(role => role.name === 'ROLE_ADMIN')">
                <router-link to="/admin" class="dropdown-item">Admin панель</router-link>
              </li>
              <li><a class="dropdown-item" href="#">Another action</a></li>
              <li>
                <hr class="dropdown-divider">
              </li>
              <li>
                <router-link class="dropdown-item" to="/logout">Выйти</router-link>
              </li>
            </ul>
          </li>
        </ul>
        <ul v-else class="navbar-nav mb-2 mb-lg-0">
          <li class="nav-item">
            <router-link class="nav-link active" aria-current="page" to="/login">Войти</router-link>
          </li>
        </ul>
      </div>
    </div>
  </nav>
</template>

<script>
export default {
  props: {
    user: {
      type: Object,
      required: false
    }
  },
  data() {
    return {
      newuser: ''
    }
  },
  watch: {
    user(newUser) {
      this.newuser = newUser;
    }
  },
}
</script>

<style scoped>
.container-fluid {
  margin-left: 20px;
  margin-right: 20px;
}
</style>