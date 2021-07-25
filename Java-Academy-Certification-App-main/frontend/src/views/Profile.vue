<template>
  <div class="profile">
    <v-row>
      <v-col cols="12">
        <v-card class="mx-auto" max-width="434" tile>
          <v-img height="100%" src="@/assets/back.jpg"> </v-img>
          <v-row style="margin-top:-8%;">
            <v-list-item style="margin-bottom:1%;">
              <v-list-item-avatar size="120">
                <img src="@/assets/user_avatar.png" alt="User" />
              </v-list-item-avatar>
              <v-list-item-content>
                <v-list-item-title class="title" style="margin-top:20px;">
                  <UpdateUserPopup />
                  {{ user.name }} {{ user.surname }}
                </v-list-item-title>
                <v-list-item-subtitle>
                  {{ user.role.name }} | {{ user.email }}</v-list-item-subtitle
                >
              </v-list-item-content>
            </v-list-item>
          </v-row>
        </v-card>
      </v-col>
    </v-row>
    <div class="manager" v-if="user.role.id === 'A' || user.role.id === 'M'">
      <v-row>
        <v-col cols="12" style="padding-top: 2em">
          <v-btn
            @click.stop="newCertificationDialog = true"
            dark
            color="primary"
            >Add New Certification</v-btn
          >
          <v-dialog
            v-model="newCertificationDialog"
            persistent
            max-width="600px"
          >
            <v-card>
              <v-card-title>
                Add New Certification
              </v-card-title>
              <v-card-text>
                <v-form ref="newCertForm" v-model="valid">
                  <v-row>
                    <v-col cols="12">
                      <v-text-field
                        label="Name"
                        v-model="certificationName"
                        :rules="nameRules"
                      ></v-text-field>
                    </v-col>
                  </v-row>
                  <v-row>
                    <v-col cols="6">
                      <v-text-field
                        label="Price"
                        v-model="certificationPrice"
                      ></v-text-field>
                    </v-col>
                    <v-col cols="6">
                      <v-text-field
                        label="Currency"
                        :rules="currencyRules"
                        v-model="certificationCurrency"
                      ></v-text-field>
                    </v-col>
                  </v-row>
                  <v-row>
                    <v-col cols="12">
                      <v-text-field
                        label="URL"
                        :rules="urlRules"
                        v-model="certificationUrl"
                      ></v-text-field>
                    </v-col>
                  </v-row>
                  <v-row>
                    <v-col>
                      <v-select
                        v-model="chosenSkillNames"
                        :items="skillNames"
                        label="Select skills"
                        outlined
                        multiple
                      ></v-select>
                    </v-col>
                  </v-row>
                </v-form>
              </v-card-text>
              <v-card-actions>
                <v-spacer />
                <v-btn
                  v-if="!added"
                  color="secondary"
                  @click="closeNewCertificationDialog"
                  >Close</v-btn
                >
                <v-btn
                  v-if="!added"
                  :disabled="!valid"
                  color="primary"
                  @click="sendNewCertificationDialog"
                  >Add</v-btn
                >
                <v-alert v-else type="success">
                  Certification added successfully.
                </v-alert>
              </v-card-actions>
            </v-card>
          </v-dialog>
        </v-col>
      </v-row>
    </div>
    <div class="admin" v-if="user.role.id === 'A'" style="text-align: left; padding: 30px">
      <v-expansion-panels focusable>
        <v-expansion-panel>
          <v-expansion-panel-header>
            <h2>List of all users:</h2>
          </v-expansion-panel-header>
          <v-expansion-panel-content>
            <div v-if="users !== undefined && users.length > 0">
              <v-row>
                <router-link
                  v-for="user in users"
                  :key="user.id"
                  :to="'/userDetail/' + user.id"
                  tag="div"
                >
                  <v-col>
                    <v-card class="mx-auto" style="cursor: pointer; margin: 10px" width="300">
                      <v-card-title>{{ user.id }}</v-card-title>
                      <v-card-text>
                        <div class="text--primary">
                          <strong
                            >Name: {{ user.name }} {{ user.surname }}</strong
                          >
                        </div>
                      </v-card-text>
                    </v-card>
                  </v-col>
                </router-link>
              </v-row>
            </div>
            <div v-else>
              <h3>No users found.</h3>
            </div>
          </v-expansion-panel-content>
        </v-expansion-panel>
      </v-expansion-panels>
    </div>
  </div>
</template>

<script>
import { mapActions } from "vuex";
import UpdateUserPopup from "@/components/UpdateUserPopup.vue";
import { mapGetters, mapMutations } from "vuex";
import axios from "axios";

export default {
  name: "Profile",
  components: {
    UpdateUserPopup,
  },
  data() {
    return {
      emailRules: [
        (value) => !!value || "Email is required",
        (value) => /.+@.+/.test(value) || "Email must be valid",
      ],
      minRules: [(value) => value.length >= 8 || "Min 8 characters"],
      valid: true,
      newCertificationDialog: false,
      added: false,
      certificationName: "",
      certificationPrice: 0,
      certificationCurrency: "",
      certificationUrl: "",
      nameRules: [v => !!v || "Name is required"],
      currencyRules: [v => !!v || "Currency is required"],
      urlRules: [v => !!v || "URL is required"],
      skills: {},
      skillNames: [],
      chosenSkillNames: []
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
        return this.$store.getters.user;
      },
      set(value) {
        this.$store.commit("userMutation", value);
      }
    },
    ...mapGetters(["users", "user"])
  },
  async mounted() {
    const { data } = await axios.get("http://localhost:8080/users");
    this.usersMutation(data._embedded.users);
    await axios
      .get("http://localhost:8080/skills")
      .then(response => (this.skills = response.data._embedded.skills));
    this.skills.forEach(skill => this.skillNames.push(skill.name));
  },
  methods: {
    edit() {
      alert("You clicked edit!");
      console.log("EDIT FUNCTION");
    },
    closeNewCertificationDialog() {
      this.newCertificationDialog = false;
      this.certificationName = "";
      this.certificationPrice = 0;
      this.certificationCurrency = "";
      this.certificationUrl = "";
      this.chosenSkillNames = [];
      this.added = false;
    },
    sendNewCertificationDialog() {
      const chosenSkills = [];
      console.log(this.chosenSkillNames);
      this.chosenSkillNames.forEach(chosenSkillName => {
        this.skills.forEach(skill => {
          if (chosenSkillName === skill.name) chosenSkills.push(skill);
        });
      });
      this.$store.dispatch("createCertificationRequest", {
        name: this.certificationName,
        currency: this.certificationCurrency,
        price: this.certificationPrice,
        url: this.certificationUrl,
        skills: chosenSkills
      });
      this.added = true;
      setTimeout(() => this.closeNewCertificationDialog(), 1500);
    },
    ...mapMutations(["usersMutation"])
  }
};
</script>
