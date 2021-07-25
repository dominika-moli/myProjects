<template>
  <div>
    <div class="student" v-if="localStorage.getItem('userRoleId') === 'S'">
      <v-card
        class="mx-auto"
        width="600"
        style="text-align: left; margin-top: 50px"
      >
        <v-card-title>
          My voucher {{ currentVoucher.voucherCode }}
        </v-card-title>
        <v-card-text class="text--primary">
          <div>State: {{ currentVoucher.state }}</div>
          <div v-if="currentVoucher.state === 'ACTIVE'">
            Valid until: {{ currentVoucher.validUntil }}
          </div>
          <div>Certification: {{ certification.name }}</div>
          <div>
            Certification URL:
            <a :href="certification.url" target="_blank">{{
              certification.url
            }}</a>
          </div>
          <div v-for="skill in skills" :key="skill.id">
            <div>Skill: {{ skill.name }}</div>
          </div>
        </v-card-text>
        <v-card-text class="text--primary">
          <h3 v-if="currentVoucher.state === 'PENDING'">
            Please wait until your voucher gets activated.
          </h3>
        </v-card-text>
        <v-card-actions v-if="currentVoucher.state === 'ACTIVE'">
          <v-btn style="margin-right: 7px" @click.stop="cancelDialog = true" color="secondary">
            Cancel Enrollment
          </v-btn>
          <v-dialog width="800" v-model="cancelDialog" persistent>
            <v-card>
              <v-card-title style="text-align: center">
                Do you really want to cancel your enrollment in
                {{ certification.name }} Certification?
              </v-card-title>
              <v-card-text style="text-align: left">
                This operation cannot be undone.
              </v-card-text>
              <v-card-actions>
                <div v-if="!cancelled">
                  <v-btn
                    style="margin-right: 5px"
                    @click="cancel"
                    color="secondary"
                  >
                    Yes! Cancel Enrollment
                  </v-btn>
                  <v-btn @click.stop="cancelDialog = false" color="primary">
                    No!
                  </v-btn>
                </div>
                <v-alert v-else type="success">
                  You cancelled your enrollment in
                  {{ certification.name }} Certification.
                </v-alert>
              </v-card-actions>
            </v-card>
          </v-dialog>
          <v-btn @click.stop="newReviewDialog = true" dark color="primary"
            >Review Certification</v-btn
          >

          <v-dialog v-model="newReviewDialog" persistent max-width="600px">
            <v-card>
              <v-card-title class="justify-center">
                Review Certification
              </v-card-title>
              <v-card-text>
                <v-form ref="newReviewForm" v-model="valid">
                  <v-row>
                    <v-col cols="12">
                      <v-input
                        style="margin: 0 auto"
                        :value="rating"
                        :rules="ratingRules"
                      >
                        <v-rating
                          style="margin: 0 auto"
                          v-model="rating"
                          middle
                        ></v-rating>
                      </v-input>
                    </v-col>
                  </v-row>

                  <v-row>
                    <v-col cols="12">
                      <v-textarea
                        label="Comment"
                        height="60"
                        :rules="commentRules"
                        v-model="comment"
                        :counter="130"
                      ></v-textarea>
                    </v-col>
                  </v-row>

                  <p v-if="error" style="color: red">
                    You have already reviewed this certification!
                  </p>
                </v-form>
              </v-card-text>
              <v-card-actions>
                <v-spacer />
                <v-btn
                  v-if="!added"
                  color="secondary"
                  @click="closeNewReviewDialog"
                  >Close</v-btn
                >
                <v-btn
                  v-if="!added"
                  :disabled="!valid"
                  color="primary"
                  @click="sendNewReviewDialog"
                  >Add</v-btn
                >
                <v-alert v-else type="success">
                  Review added successfully.
                </v-alert>
              </v-card-actions>
            </v-card>
          </v-dialog>
        </v-card-actions>
      </v-card>
    </div>
    <div
      class="manager"
      v-else-if="
        localStorage.getItem('userRoleId') === 'M' ||
          localStorage.getItem('userRoleId') === 'A'
      "
    >
      <v-card
        v-if="currentVoucher.state === 'PENDING'"
        class="mx-auto"
        width="600"
        style="text-align: left; margin-top: 50px"
      >
        <v-card-title>
          Pending voucher to activate: {{ currentVoucher.voucherCode }}
        </v-card-title>
        <v-card-text class="text--primary">
          <div>
            Requested by: {{ requestedBy.name }} {{ requestedBy.surname }} (ID:
            {{ requestedBy.id }}), {{ requestedBy.email }}
          </div>
        </v-card-text>
        <v-card-actions style="padding: 15px">
          <v-btn v-if="!voucherAccepted" @click="accept" color="primary" dark
            >Activate</v-btn
          >
          <v-alert v-else type="success">
            {{ currentVoucher.voucherCode }} successfully activated.
          </v-alert>
        </v-card-actions>
      </v-card>
    </div>
    <div class="admin" v-if="localStorage.getItem('userRoleId') === 'A'">
      <v-card
        v-if="currentVoucher.state === 'ACTIVE'"
        class="mx-auto"
        width="600"
        style="text-align: left; margin-top: 50px"
      >
        <v-card-title>
          <h2>Active Voucher: {{ currentVoucher.voucherCode }}</h2>
          <span>Valid until: {{ currentVoucher.validUntil }}</span>
        </v-card-title>
        <v-card-actions style="padding: 15px">
          <v-btn @click.stop="deleteDialog = true" color="red darken-1" dark
            >Delete</v-btn
          >
          <v-dialog width="800" v-model="deleteDialog" persistent>
            <v-card>
              <v-card-title style="text-align: center">
                Do you really want to delete voucher {{ currentVoucher.voucherCode }}?
              </v-card-title>
              <v-card-text style="text-align: left">
                This operation cannot be undone.
              </v-card-text>
              <v-card-actions>
                <div v-if="!cancelled">
                  <v-btn
                    style="margin-right: 5px"
                    @click="cancel"
                    color="secondary"
                  >
                    Yes! Delete voucher
                  </v-btn>
                  <v-btn @click.stop="deleteDialog = false" color="primary">
                    No!
                  </v-btn>
                </div>
                <v-alert v-else type="success">
                  {{ currentVoucher.voucherCode }} successfully deleted.
                </v-alert>
              </v-card-actions>
            </v-card>
          </v-dialog>
        </v-card-actions>
      </v-card>
    </div>
  </div>
</template>

<script>
import { mapGetters } from "vuex";
import axios from "axios";

export default {
  name: "ManagerVoucherDetail",
  data() {
    return {
      localStorage,
      currentVoucher: {},
      certification: {},
      skills: [],
      voucherAccepted: false,
      requestedBy: {},
      cancelDialog: false,
      deleteDialog: false,
      cancelled: false,
      newReviewDialog: false,
      valid: true,
      added: false,
      error: false,
      comment: "",
      rating: 0,
      commentRules: [
        (value) => value.length >= 3 || "Min 3 characters",
        (value) => value.length <= 130 || "Max 130 characters",
      ],
      ratingRules: [(value) => value > 0]
    };
  },
  computed: {
    ...mapGetters(["vouchers"])
  },
  async mounted() {
    const { data } = await axios.get(
      "http://localhost:8080/vouchers/" + this.$route.params.id
    );
    this.currentVoucher = data;
    await axios
      .get(
        "http://localhost:8080/vouchers/" +
          this.$route.params.id +
          "/certification"
      )
      .then(response => (this.certification = response.data));
    await axios
      .get(
        "http://localhost:8080/certifications/" +
          this.certification.id +
          "/skills"
      )
      .then(response => (this.skills = response.data._embedded.skills));
    await axios
      .get("http://localhost:8080/vouchers/" + this.$route.params.id + "/user")
      .then(response => (this.requestedBy = response.data));
  },
  methods: {
    accept() {
      this.$store.dispatch("createAcceptVoucherRequest", {
        id: this.currentVoucher.id
      });
      this.voucherAccepted = true;
      setTimeout(() => this.$router.push("/"), 1500);
    },
    cancel() {
      this.$store.dispatch("createCancelVoucherRequest", {
        id: this.currentVoucher.id
      });
      this.cancelled = true;
      if (this.localStorage.getItem("userRoleId") === "A")
        setTimeout(() => this.$router.push("/profile"), 1500);
      else setTimeout(() => this.$router.push("/"), 1500);
    },
    closeNewReviewDialog() {
      this.newReviewDialog = false;
      this.added = false;
      this.comment = "";
      this.rating = 0;
      this.error = false;
    },
    sendNewReviewDialog() {
      this.addReview();
    },
    async addReview() {
      console.log(this.comment);
      console.log(this.rating);
      const url =
        "http://localhost:8080/add-review/" +
        this.$store.getters.user.id +
        "/" +
        this.certification.id +
        "?comment=" +
        this.comment +
        "&rating=" +
        this.rating;
      const body = {
        comment: this.comment,
        rating: this.rating,
        posted: new Date(),
        user: this.$store.getters.user,
        certification: this.currentCertification,
      };
      console.log(url);
      console.log(body);
      try {
        const { data } = await axios.post(url, body);
        console.log(data);
        this.added = true;
        setTimeout(() => this.closeNewReviewDialog(), 1500);
      } catch (err) {
        console.log(err);
        this.error = true;
      }
    }
  }
};
</script>
