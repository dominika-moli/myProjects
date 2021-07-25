<template>
  <v-container fluid fill-height>
    <v-layout align-center justify-center>
      <v-flex md4 sm8 xs12>
        <v-card class="elevation-12">
          <v-toolbar dark color="black">
            <v-toolbar-title
              >Welcome to Certifications app - Reset Password</v-toolbar-title
            >
          </v-toolbar>
          <v-card-text>
            <p :style="{ color: resetColor }">
              {{ resetInfo }}
            </p>
            <v-form ref="form" v-model="validResetForm">
              <v-text-field
                label="Email"
                type="text"
                v-model="resetEmail"
                prepend-icon="mdi-email"
                :rules="emailRules"
              >
              </v-text-field>
            </v-form>
          </v-card-text>
          <v-card-actions class="justify-center">
            <v-btn
              color="primary"
              width="120"
              @click="resetPass"
              :disabled="!validResetForm"
              >Send email</v-btn
            >
          </v-card-actions>
        </v-card>
      </v-flex>
    </v-layout>
  </v-container>
</template>

<script>
import { mapActions } from "vuex";
import axios from "axios";

export default {
  name: "ResetPassword",
  resetInfo: "Please Enter your credentials.",
  resetColor: "",
  data() {
    return {
      validResetForm: true,
      resetEmail: "",
      resetColor: "",
      emailRules: [
        (value) => !!value || "Email is required",
        (value) => /.+@.+/.test(value) || "Email must be valid",
      ],
    };
  },

  methods: {
    async resetPass() {
      const url =
        "http://localhost:8080/forgot_password?email=" + this.resetEmail;

      try {
        const { data } = await axios.post(url);
        console.log(data);
        this.resetInfo = "Email sent. Check your mailbox for more.";
        this.resetColor = "green";
      } catch (err) {
        console.log(err);
        this.resetInfo = "Email not found. Try again.";
        this.resetColor = "red";
      }
    },
  },
};
</script>
