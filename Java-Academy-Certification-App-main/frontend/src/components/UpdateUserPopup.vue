<template>
  <div class="text-end" style="padding-right:5%; margin-top:5%">
    <v-dialog max-width="600px" v-model="dialog" persistent>
      <template v-slot:activator="{ on }">
        <v-btn v-on="on" outlined small fab color="orange">
          <v-icon>mdi-pencil</v-icon>
        </v-btn>
      </template>
      <v-card>
        <v-card-title class="justify-center">
          <h4>My Profile</h4>
        </v-card-title>
        <v-card-text>
          <v-form class="px-3" ref="updateUserForm" v-model="valid">
            <v-text-field
              label="Name"
              v-model="name"
              prepend-icon="mdi-account-question"
              :rules="minRulesName"
            >
            </v-text-field>
            <v-text-field
              label="Surname"
              v-model="surname"
              prepend-icon="mdi-account-question"
              :rules="minRulesName"
            >
            </v-text-field>
            <v-text-field
              prepend-icon="mdi-lock"
              label="Old Password"
              type="password"
              :rules="minRules"
              v-model="oldPass"
            >
            </v-text-field>

            <v-text-field
              prepend-icon="mdi-lock"
              label="New Password"
              type="password"
              :rules="minRules"
              v-model="newPass1"
            >
            </v-text-field>

            <v-text-field
              prepend-icon="mdi-lock"
              label="New Password"
              type="password"
              :rules="minRules"
              v-model="newPass2"
            >
            </v-text-field>
          </v-form>
        </v-card-text>

        <v-card-actions>
          <v-spacer />
          <v-btn color="secondary" @click="closeUpdateUserDialog">Close</v-btn>
          <v-btn
            :disabled="!valid"
            color="primary"
            @click="sendUpdateUserDialog"
            >Update</v-btn
          >
        </v-card-actions>
      </v-card>
    </v-dialog>
  </div>
</template>

<script>
import axios from "axios";
import { mapGetters, mapMutations } from "vuex";

export default {
  data() {
    return {
      name: this.$store.getters.user.name,
      surname: this.$store.getters.user.surname,
      oldPass: "",
      newPass1: "",
      newPass2: "",
      minRules: [(value) => value.length >= 8 || "Min 8 characters"],
      minRulesName: [(value) => value.length >= 1 || "Min 1 characters"],
      valid: true,
      dialog: false,
    };
  },
  computed: {
    email: {
      get() {
        return this.$store.getters.email;
      },
      set(value) {
        this.$store.commit("emailMutation", value);
      },
    },
    password: {
      get() {
        return this.$store.getters.password;
      },
      set(value) {
        this.$store.commit("passwordMutation", value);
      },
    },
    user: {
      get() {
        console.log("chichi");
        console.log(this.$store.getters.user.name);
        return this.$store.getters.user;
      },
      set(value) {
        this.$store.commit("userMutation", value);
      },
    },
  },
  methods: {
    closeUpdateUserDialog() {
      (this.oldPass = ""),
        (this.newPass1 = ""),
        (this.newPass2 = ""),
        (this.dialog = false);
    },
    async sendUpdateUserDialog({ commit, rootState }) {
      const url =
        "http://localhost:8080/update-user/" + this.$store.getters.user.id;

      let body = {};
      console.log(this.oldPass);
      console.log(this.$store.getters.password);
      if (this.oldPass == this.$store.getters.password) {
        if (this.newPass1 == this.newPass2) {
          console.log("juhuu rovnaju sa");
          body = {
            name: this.name,
            surname: this.surname,
            password: this.newPass2,
            email: this.$store.getters.email,
          };
        } else console.log("nerovnaju sa hesla");
      } else {
        console.log("povodne heslo nesedi");
        body = {
          name: this.name,
          surname: this.surname,
          email: this.$store.getters.email,
        };
      }

      try {
        const { data } = await axios.put(url, body);
        console.log(data);
        this.$store.commit("userMutation", data);
      } catch (err) {
        console.log(err);
      }
      this.closeUpdateUserDialog();
    },
  },
  ...mapMutations(["userMutation"]),
};
</script>

<style></style>
