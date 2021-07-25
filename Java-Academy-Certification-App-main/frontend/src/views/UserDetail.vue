<template>
  <div class="admin" v-if="user.role.id === 'A'">
    <v-card
      class="mx-auto"
      width="600"
      style="text-align: left; margin-top: 50px"
    >
      <v-card-title>
        <h2>User {{ currentUser.id }}</h2>
      </v-card-title>
      <v-card-text>
        <strong class="text--primary">Name: {{ currentUser.name }} {{ currentUser.surname }}</strong>
        <div class="text--primary">Email: {{ currentUser.email }}</div>
        <div class="text--primary">Role: {{ currentRole.name }}</div>
      </v-card-text>
      <v-card-actions style="padding: 15px">
        <v-row>
          <v-col cols="12">
            <v-select
              v-model="newRoleName"
              :items="roles"
              label="Select role"
              outlined
            ></v-select>
          </v-col>
          <v-col cols="12">
            <v-btn
              v-if="!roleChanged && !cannotChange"
              @click="changeRole"
              color="primary"
              >Update</v-btn
            >
            <v-alert v-else-if="roleChanged" type="success">
              Role changed to {{ newRoleName }}
            </v-alert>
            <div v-else-if="cannotChange">
              <v-alert v-if="newRoleName" type="warning">
                Cannot change role to {{ newRoleName }}.
              </v-alert>
              <v-alert v-else type="warning">
                Cannot change role.
              </v-alert>
            </div>
          </v-col>
        </v-row>
      </v-card-actions>
    </v-card>

    <div>
      <!--All available vouchers for selected user-->
      <v-row>
        <v-col cols="12">
          <div v-if="currentVouchers !== undefined && currentVouchers.length > 0">
          <h1>Vouchers</h1>
            <v-row>
              <router-link
                v-for="voucher in currentVouchers"
                :key="voucher.id"
                :to="'/voucherDetail/' + voucher.id"
                tag="div"
              >
                <v-col>
                  <VoucherItem
                    style="cursor: pointer"
                    :voucher="voucher"
                  ></VoucherItem>
                </v-col>
              </router-link>
            </v-row>
          </div>
          <div v-else>
            <h3>No vouchers found</h3>
          </div>
        </v-col>
      </v-row>
    </div>

  </div>
</template>

<script>
import { mapGetters } from "vuex";
import axios from "axios";
import VoucherItem from "@/components/VoucherItem.vue";

export default {
  name: "UserDetail",
  components: {
    VoucherItem,
  },
  data() {
    return {
      currentUser: {},
      currentRole: {},
      newRoleName: {},
      roleChanged: false,
      cannotChange: false,
      currentVouchers: {},
      roles: ["Student", "Manager", "Administrator"]
    };
  },
  created() {
    this.currentUser = this.users.find(({ id }) => id == this.$route.params.id);
  },
  computed: {
    ...mapGetters(["users", "user"]),
  },
  methods: {
    async changeRole() {
      if (
        this.newRoleName == null ||
        this.newRoleName.toUpperCase() === this.currentRole.name
      ) {
        this.roleChanged = false;
        this.cannotChange = true;
        setTimeout(() => (this.cannotChange = false), 1500);
        return false;
      } else {
        await this.$store.dispatch("createChangeRoleRequest", {
          id: this.currentUser.id,
          role: this.newRoleName.toUpperCase()
        });
        this.roleChanged = true;
        this.cannotChange = false;
        setTimeout(() => this.$router.push("/profile"), 1500);
        return true;
      }
    }
  },
  async mounted() {

    axios.get("http://localhost:8080/users/" + this.currentUser.id + "/vouchers").then(response => {
      this.currentVouchers = response.data._embedded.vouchers;
      // console.log("current vouchers");
      // console.log(this.currentVouchers);
    });

    const url = "http://localhost:8080/users/" + this.currentUser.id + "/role";
    const { data } = await axios.get(url);
    this.currentRole = data;
    this.newRoleName = data.name;
    console.log("current role");
    console.log(this.currentRole);
    console.log(this.newRoleName);
  }
};
</script>

<style scoped></style>
