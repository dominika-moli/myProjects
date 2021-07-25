<template>
  <v-app-bar app dark>
    <router-link to="/" tag="span" class="pointerClass">
      <h2>Certifications@IBM</h2>
    </router-link>
    <v-spacer />
    <v-container>
      <v-row>
        <v-col cols="12">
          <span>Hello, {{ user.name }}!</span>
          <v-row no-gutters>
            <v-col cols="12">    
            <router-link to="/profile" tag="span" class="pointerClass" style="font-size: 130%;">
              <span>Profile</span>
              <v-icon>mdi-account</v-icon>
            </router-link>
            </v-col>
          </v-row>
        </v-col>
      </v-row>
    </v-container>

    <v-spacer />
    <span class="pointerClass" @click="logout">
      <span>Logout</span>
      <v-icon>mdi-logout</v-icon>
    </span>
    <div style="padding: 30px">
      <v-tooltip v-if="!$vuetify.theme.dark" bottom>
        <template v-slot:activator="{ on }">
          <v-btn v-on="on" color="info" small fab @click="darkMode">
            <v-icon class="mr-1">mdi-moon-waxing-crescent</v-icon>
          </v-btn>
        </template>
        <span>Dark Mode</span>
      </v-tooltip>

      <v-tooltip v-else bottom>
        <template v-slot:activator="{ on }">
          <v-btn v-on="on" color="info" small fab @click="darkMode">
            <v-icon color="yellow">mdi-white-balance-sunny</v-icon>
          </v-btn>
        </template>
        <span>Light Mode</span>
      </v-tooltip>
    </div>
  </v-app-bar>
  
</template>

<script>
export default {
  name: "Header",
  methods: {
    logout() {
      this.$router.push("/login");
      localStorage.removeItem("token");
      location.reload();
    },
    darkMode() {
      this.$vuetify.theme.dark = !this.$vuetify.theme.dark;
    }

  },
  props: {
    attrs: {
      type: Object,
      default: () => ({}),
    },
  },
  computed: {
    user: {
      get() {
        return this.$store.getters.user;
      },
    },
  },

};
</script>

<style scoped>
.pointerClass {
  cursor: pointer;
}
</style>
