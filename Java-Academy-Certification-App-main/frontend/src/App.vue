<template>
  <v-app>
    <Header v-if="loggedIn" />
    <v-main>
      <router-view />
    </v-main>
    <Footer />
  </v-app>
</template>

<script>
import Footer from "./components/Footer.vue";
import Header from "./components/Header.vue";
import axios from "axios";
import { mapGetters, mapMutations } from "vuex";

export default {
  components: {
    Footer,
    Header,
  },

  data() {
    return {
      token: "",
    };
  },

  computed: {
    ...mapGetters(["loggedIn"]),
  },

  async created() {
    this.token = localStorage.getItem("token");
    console.log(this.token);

    const url = "http://localhost:8080/sign-in-token?token=" + this.token;

    try {
      const { data } = await axios.post(url);
      console.log(data);
      this.$store.commit("userMutation", data.user);
      localStorage.setItem("userRoleId", data.user.role.id);
      localStorage.setItem("userRoleId", data.user.role.id);
      this.loggedInMutation(true);
    } catch (err) {
      console.log(err);
      console.log(this.$route);
      if (this.$route.name !== "ChangePassword") this.$router.push("/login");
    }
  },

  methods: {
    ...mapMutations(["loggedInMutation"]),
  },
};
</script>

<style lang="scss">
#app {
  font-family: Avenir, Helvetica, Arial, sans-serif;
  -webkit-font-smoothing: antialiased;
  -moz-osx-font-smoothing: grayscale;
  text-align: center;
  color: #2c3e50;
}
</style>
