<template>
  <div>
    <el-card style="width: 50%">
      <el-form :model="user" label-width="100px" style="padding-right: 50px" :rules="formRules" ref="userForm">
        <div style="margin: 15px; text-align: center">
          <el-upload
              class="avatar-uploader"
              :action="$baseUrl + '/files/upload'"
              :show-file-list="false"
              :on-success="handleAvatarSuccess"
          >
            <img v-if="user.avatar" :src="user.avatar" class="avatar" />
            <i v-else class="el-icon-plus avatar-uploader-icon"></i>
          </el-upload>
        </div>
        <el-form-item label="用户名" prop="username">
          <el-input v-model="user.username" placeholder="用户名" disabled></el-input>
        </el-form-item>
        <el-form-item label="酒店名称" prop="name">
          <el-input v-model="user.name" placeholder="酒店名称"></el-input>
        </el-form-item>
        <el-form-item label="电话" prop="phone">
          <el-input v-model="user.phone" placeholder="电话"></el-input>
        </el-form-item>
        <el-form-item label="邮箱" prop="email">
          <el-input v-model="user.email" placeholder="邮箱"></el-input>
        </el-form-item>
        <el-form-item label="价格" prop="price">
          <el-input v-model="user.price" placeholder="价格"></el-input>
        </el-form-item>
        <el-form-item label="官网" prop="url">
          <el-input v-model="user.url" placeholder="官网"></el-input>
        </el-form-item>
        <el-form-item label="介绍" prop="description">
          <el-input type="textarea" v-model="user.description" placeholder="介绍"></el-input>
        </el-form-item>
        <div style="text-align: center; margin-bottom: 20px">
          <el-button type="primary" @click="update">保 存</el-button>
        </div>
      </el-form>
    </el-card>
  </div>
</template>

<script>
import { validatePhone, validateEmail, validateUrl } from '@/utils/validation'; // 引入验证规则

export default {
  name: "AdminPerson",
  data() {
    return {
      user: JSON.parse(localStorage.getItem('xm-user') || '{}'),
      formRules: {
        name: [
          { required: true, message: '请输入酒店名称', trigger: 'blur' }
        ],
        phone: [
          { validator: (rule, value, callback) => {
              if (value && !validatePhone(value)) {
                callback(new Error('电话格式不正确'));
              } else {
                callback();
              }
            }, trigger: 'blur'
          }
        ],
        email: [
          { validator: (rule, value, callback) => {
              if (value && !validateEmail(value)) {
                callback(new Error('邮箱格式不正确'));
              } else {
                callback();
              }
            }, trigger: 'blur'
          }
        ],
        url: [
          { validator: (rule, value, callback) => {
              if (value && !validateUrl(value)) {
                callback(new Error('网址格式不正确'));
              } else {
                callback();
              }
            }, trigger: 'blur'
          }
        ],
        price: [
          { validator: (rule, value, callback) => {
              if (value && isNaN(value)) {
                callback(new Error('价格必须是数字'));
              } else {
                callback();
              }
            }, trigger: 'blur'
          }
        ]
      }
    };
  },
  created() {
  },
  methods: {
    update() {
      this.$refs.userForm.validate((valid) => {
        if (valid) {
          this.$request.put('/hotel/update', this.user).then(res => {
            if (res.code === '200') {
              this.$message.success('保存成功');
              localStorage.setItem('xm-user', JSON.stringify(this.user));
              this.$emit('update:user');
            } else {
              this.$message.error(res.msg);
            }
          });
        } else {
          this.$message.error('请检查输入内容是否正确');
        }
      });
    },
    handleAvatarSuccess(response, file, fileList) {
      this.$set(this.user, 'avatar', response.data);
    },
  }
}
</script>

<style scoped>
/deep/.el-form-item__label {
  font-weight: bold;
}
/deep/.el-upload {
  border-radius: 50%;
}
/deep/.avatar-uploader .el-upload {
  border: 1px dashed #d9d9d9;
  cursor: pointer;
  position: relative;
  overflow: hidden;
  border-radius: 50%;
}
/deep/.avatar-uploader .el-upload:hover {
  border-color: #409EFF;
}
.avatar-uploader-icon {
  font-size: 28px;
  color: #8c939d;
  width: 120px;
  height: 120px;
  line-height: 120px;
  text-align: center;
  border-radius: 50%;
}
.avatar {
  width: 120px;
  height: 120px;
  display: block;
  border-radius: 50%;
}
</style>
