<template>
  <div class="main-content">
    <el-card style="width: 50%; margin: 30px auto">
      <div style="text-align: right; margin-bottom: 20px">
        <el-button type="primary" @click="updatePassword">修改密码</el-button>
      </div>
      <el-form :model="user" label-width="80px" style="padding-right: 20px" :rules="formRules" ref="userForm">
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
        <el-form-item label="昵称" prop="name">
          <el-input v-model="user.name" placeholder="昵称"></el-input>
        </el-form-item>
        <el-form-item label="电话" prop="phone">
          <el-input v-model="user.phone" placeholder="电话"></el-input>
        </el-form-item>
        <el-form-item label="邮箱" prop="email">
          <el-input v-model="user.email" placeholder="邮箱"></el-input>
        </el-form-item>
        <div style="text-align: center; margin-bottom: 20px">
          <el-button type="primary" @click="update">保 存</el-button>
        </div>
      </el-form>
    </el-card>
    <el-dialog title="修改密码" :visible.sync="dialogVisible" width="30%" :close-on-click-modal="false" destroy-on-close>
      <el-form :model="user" label-width="80px" style="padding-right: 20px" :rules="passwordRules" ref="passwordForm">
        <el-form-item label="原始密码" prop="password">
          <el-input show-password v-model="user.password" placeholder="原始密码"></el-input>
        </el-form-item>
        <el-form-item label="新密码" prop="newPassword">
          <el-input show-password v-model="user.newPassword" placeholder="新密码"></el-input>
        </el-form-item>
        <el-form-item label="确认密码" prop="confirmPassword">
          <el-input show-password v-model="user.confirmPassword" placeholder="确认密码"></el-input>
        </el-form-item>
      </el-form>
      <div slot="footer" class="dialog-footer">
        <el-button @click="dialogVisible = false">取 消</el-button>
        <el-button type="primary" @click="save">确 定</el-button>
      </div>
    </el-dialog>
  </div>
</template>

<script>
import {
  validateNickname,
  validatePhone,
  validateEmail,
  validatePassword
} from '@/utils/validation'; // 引入验证规则

export default {
  data() {
    // 验证确认密码
    const validatePasswordConfirm = (rule, value, callback) => {
      if (value === '') {
        callback(new Error('请确认密码'));
      } else if (value !== this.user.newPassword) {
        callback(new Error('两次输入的密码不一致'));
      } else {
        callback();
      }
    };

    return {
      user: JSON.parse(localStorage.getItem('xm-user') || '{}'),
      dialogVisible: false,

      formRules: {
        name: [
          { validator: (rule, value, callback) => {
              if (value && !validateNickname(value)) {
                callback(new Error('昵称最多12个字符'));
              } else {
                callback();
              }
            }, trigger: 'blur'
          }
        ],
        phone: [
          { validator: (rule, value, callback) => {
              if (value && !validatePhone(value)) {
                callback(new Error('请输入正确的手机号'));
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
        ]
      },
      passwordRules: {
        password: [
          { required: true, message: '请输入原始密码', trigger: 'blur' }
        ],
        newPassword: [
          { required: true, message: '请输入新密码', trigger: 'blur' },
          { validator: (rule, value, callback) => {
              if (!validatePassword(value)) {
                callback(new Error('密码为6-12位，必须包含大/小写字母、符号、数字中的至少两个'));
              } else {
                callback();
              }
            }, trigger: 'blur'
          }
        ],
        confirmPassword: [
          { validator: validatePasswordConfirm, required: true, trigger: 'blur' }
        ]
      }
    };
  },
  methods: {
    update() {
      this.$refs.userForm.validate((valid) => {
        if (valid) {
          this.$request.put('/user/update', this.user).then(res => {
            if (res.code === '200') {
              this.$message.success('保存成功');
              localStorage.setItem('xm-user', JSON.stringify(this.user));
              this.$emit('update:user');
            } else {
              this.$message.error(res.msg);
            }
          });
        } else {
          this.$message.error('请检查输入内容');
        }
      });
    },
    handleAvatarSuccess(response, file, fileList) {
      this.$set(this.user, 'avatar', response.data);
    },
    updatePassword() {
      this.dialogVisible = true;
    },
    save() {
      this.$refs.passwordForm.validate((valid) => {
        if (valid) {
          this.$request.put('/updatePassword', this.user).then(res => {
            if (res.code === '200') {
              this.$message.success('修改密码成功');
              this.$router.push('/login');
            } else {
              this.$message.error(res.msg);
            }
          });
        } else {
          this.$message.error('请检查密码是否正确');
        }
      });
    }
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
