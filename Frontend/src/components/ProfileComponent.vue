<template>
  <div class="container">
    <div class="main-body">
      <div class="slot1">
        <slot name="slot1"></slot>
      </div>
      <div class="row gutters-sm">
        <div class="col-md-4 mb-3">
          <custom-card>
            <div class="card-body">
              <div class="d-flex flex-column align-items-center text-center">
                <img src="https://bootdey.com/img/Content/avatar/avatar7.png" alt="Admin" class="rounded-circle"
                     width="150">
                <div class="mt-3">
                  <h4 v-if="this.fullName!==null">{{ this.fullName }}</h4>
                  <h4 v-else>{{ this.login }}</h4>
                  <hr>
                  <p class="text-secondary mb-1" v-for="role in roles" :key="role.name">{{ role.name }}</p>
                  <!--                  <button class="btn btn-primary">Follow</button>-->
                  <!--                  <button class="btn btn-outline-primary">Message</button>-->
                </div>
              </div>
            </div>
          </custom-card>
          <custom-card class="mt-3">
            <ul class="list-group list-group-flush">
              <li class="list-group-item d-flex justify-content-between align-items-center flex-wrap">
                <h6 class="mb-0">
                  <svg xmlns="http://www.w3.org/2000/svg" width="24" height="24" viewBox="0 0 24 24" fill="none"
                       stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round"
                       class="feather feather-globe mr-2 icon-inline">
                    <circle cx="12" cy="12" r="10"></circle>
                    <line x1="2" y1="12" x2="22" y2="12"></line>
                    <path
                        d="M12 2a15.3 15.3 0 0 1 4 10 15.3 15.3 0 0 1-4 10 15.3 15.3 0 0 1-4-10 15.3 15.3 0 0 1 4-10z"></path>
                  </svg>
                  Website
                </h6>
                <span class="text-secondary">https://4412.ru</span>
              </li>
              <li class="list-group-item d-flex justify-content-between align-items-center flex-wrap">
                <h6 class="mb-0">
                  <svg xmlns="http://www.w3.org/2000/svg" width="24" height="24" viewBox="0 0 24 24" fill="none"
                       stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round"
                       class="feather feather-github mr-2 icon-inline">
                    <path
                        d="M9 19c-5 1.5-5-2.5-7-3m14 6v-3.87a3.37 3.37 0 0 0-.94-2.61c3.14-.35 6.44-1.54 6.44-7A5.44 5.44 0 0 0 20 4.77 5.07 5.07 0 0 0 19.91 1S18.73.65 16 2.48a13.38 13.38 0 0 0-7 0C6.27.65 5.09 1 5.09 1A5.07 5.07 0 0 0 5 4.77a5.44 5.44 0 0 0-1.5 3.78c0 5.42 3.3 6.61 6.44 7A3.37 3.37 0 0 0 9 18.13V22"></path>
                  </svg>
                  Github
                </h6>
                <span class="text-secondary">omon4412</span>
              </li>
            </ul>
          </custom-card>
        </div>
        <div class="col-md-8">
          <custom-card>
            <div class="card-body">
              <div class="row">
                <div class="col-sm-3">
                  <h6 class="mb-0">ФИО</h6>
                </div>
                <div class="col-sm-9 text-secondary">
                  {{ this.fullName }}
                </div>
              </div>
              <hr>
              <div class="row">
                <div class="col-sm-3">
                  <h6 class="mb-0">Логин</h6>
                </div>
                <div class="col-sm-9 text-secondary">
                  {{ this.login }}
                </div>
              </div>
              <hr>
              <div class="row">
                <div class="col-sm-3">
                  <h6 class="mb-0">Email</h6>
                </div>
                <div class="col-sm-9 text-secondary">
                  {{ this.email }}
                </div>
              </div>
              <hr>
              <div class="row">
                <div class="col-sm-3">
                  <h6 class="mb-0">Телефон</h6>
                </div>
                <div class="col-sm-9 text-secondary">
                  {{ this.phone }}
                </div>
              </div>
              <!--              <hr>-->
              <!--              <div class="row">-->
              <!--                <div class="col-sm-12">-->
              <!--                  <a class="btn btn-info" target="__blank"-->
              <!--                     href="https://www.bootdey.com/snippets/view/profile-edit-data-and-skills">Редактировать (NI)</a>-->
              <!--                </div>-->
              <!--              </div>-->
            </div>
          </custom-card>
          <div class="slot2">
            <slot name="slot2"></slot>
          </div>
        </div>

      </div>

    </div>
  </div>
</template>

<script>

export default {
  props: {
    fullName: {type: String},
    login: {type: String},
    email: {type: String},
    phone: {type: String},
    roles: {type: Array},
  },
}
</script>

<style scoped>
.container {
  height: calc(100vh - 113px);
}

.main-body {
  padding: 15px;
}

.card-body {
  flex: 1 1 auto;
  min-height: 1px;
  padding: 1rem;
}

.gutters-sm {
  margin-right: -8px;
  margin-left: -8px;
}

.gutters-sm > .col, .gutters-sm > [class*=col-] {
  padding-right: 8px;
  padding-left: 8px;
}

.mb-3, .my-3 {
  margin-bottom: 1rem !important;
}

.btn-info {
  background-color: #34495e !important;
  border-color: #34495e !important;
}

.bg-gray-300 {
  background-color: #e2e8f0;
}

.h-100 {
  height: 100% !important;
}

.shadow-none {
  box-shadow: none !important;
}
</style>