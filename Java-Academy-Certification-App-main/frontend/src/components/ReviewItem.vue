<template>
  <div class="card-padding">
    <v-card width="380">
      <div style="height: 50px; padding: 10px">
        <div style="padding-top: 10px">
          <p style="color: black; font-size: 24px; text-align: left">
            {{ review.user.name }} {{ review.user.surname }}
            <span style="padding-top: 8px; color: grey; font-size: 16px; float: right">
              {{ format(review.posted) }}
            </span>
          </p>
        </div>
      </div>

      <div style="word-wrap: break-word; height: 120px">
        <v-card-text class="fill-height">
          <v-rating v-model="rating" middle readonly></v-rating>
          <p>
            {{ review.comment }}
          </p>
        </v-card-text>
      </div>
    </v-card>
  </div>
</template>

<script>
import moment from "moment";

export default {
  data() {
    return {
      rating: this.review.rating,
    };
  },

  props: {
    review: {
      type: Object,
      required: true,
    },
  },

  methods: {
    format(value) {
      if (value) {
        return moment(String(value)).format("DD.MM.YYYY");
      }
    },
  },
};
</script>

<style lang="scss" scoped>
.card-padding {
  padding: 30px;
}
</style>
