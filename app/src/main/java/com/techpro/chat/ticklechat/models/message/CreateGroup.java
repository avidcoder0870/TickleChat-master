package com.techpro.chat.ticklechat.models.message;

import java.io.Serializable;

/**
 * Created by Sagar on 10/30/2016.
 */

public class CreateGroup implements Serializable{
    private String message;
    private String status;
    private GroupDetails body;

    public String getStatus() {return status;}
    public void setStatus(String status) {this.status = status;}
    public String getMessage() {
        return message;
    }
    public void setMessage(String message) {
        this.message = message;
    }
    public GroupDetails getBody() {
        return body;
    }
    public void setBody(GroupDetails body) {this.body = body;}


    @Override
    public String toString() {
        return "UserModel [message = " + message + ", body = " + body + ", status = " + status + "]";
    }


    public class GroupDetails  implements Serializable{

        private String id;
        private String name;
        private String image;
        private String created_by;
        private String admin;
        private String created_at;
        private String group_image;
        private String updated_at;

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getImage() {
            return image;
        }

        public void setImage(String image) {
            this.image = image;
        }

        public String getCreated_by() {
            return created_by;
        }

        public void setCreated_by(String created_by) {
            this.created_by = created_by;
        }

        public String getAdmin() {
            return admin;
        }

        public void setAdmin(String admin) {
            this.admin = admin;
        }

        public String getCreated_at() {
            return created_at;
        }

        public void setCreated_at(String created_at) {
            this.created_at = created_at;
        }

        public String getGroup_image() {
            return group_image;
        }

        public void setGroup_image(String group_image) {
            this.group_image = group_image;
        }

        public String getUpdated_at() {
            return updated_at;
        }

        public void setUpdated_at(String updated_at) {
            this.updated_at = updated_at;
        }
    }
}
