#include<iostream>
#include <string>
#include <cstring>
#include <unistd.h>
#include <sys/socket.h>
#include <netinet/in.h>
#include <arpa/inet.h>
#include <mysql/mysql.h>
#include <netinet/tcp.h>
#include <fcntl.h>
// ====================== 宏定义（配置项）======================
// MySQL 主机地址
#define MYSQL_HOST "localhost"
// MySQL 用户名
#define MYSQL_USER "root"
// MySQL 密码
#define MYSQL_PWD  "030625"
// MySQL 数据库名
#define MYSQL_DB   "face_attendance"
// TCP 服务器端口
#define TCP_PORT   8888
// 接收缓冲区大小
#define BUF_SIZE   4096
using namespace std;

//创建一个类用来运行命令构造函数参数就是命令和套接字
//另一个类用来生成套接字构造函数为端口号？
class face{
    string name;

};
MYSQL* initMysqlConn() {
    // 初始化MySQL对象
    MYSQL* conn = mysql_init(NULL);
    if (!conn) return NULL;  // 初始化失败直接返回

    // 连接MySQL数据库：主机、用户、密码、库名、端口、套接字、标志
    if (!mysql_real_connect(conn, MYSQL_HOST, MYSQL_USER, MYSQL_PWD, MYSQL_DB, 3300, NULL, 0)) {
        mysql_close(conn);   // 连接失败关闭
        return NULL;
    }

    // 设置字符集为 utf8mb4（支持表情、中文）
    mysql_set_character_set(conn, "utf8mb4");
    return conn;  // 返回可用连接
}




int main(){
    int server_fd = socket(AF_INET, SOCK_STREAM, 0);
    if (server_fd == -1) {
        perror("socket failed");
        return -1;
    }
    MYSQL* conn = initMysqlConn();//初始化数据库
    // 2. 绑定地址和端口
    sockaddr_in addr;
    addr.sin_family = AF_INET;
    addr.sin_port = htons(8886);          // 端口 8888
    addr.sin_addr.s_addr = INADDR_ANY;    // 监听所有网卡

    if (bind(server_fd, (sockaddr*)&addr, sizeof(addr)) == -1) {
        perror("bind failed");
        close(server_fd);
        return -1;
    }

    // 3. 监听
    listen(server_fd, 5);
    while(1){
    cout << "等待客户端连接..." << endl;

    // 4. 接受连接
    sockaddr_in client_addr;
    socklen_t len = sizeof(client_addr);
    int client_fd = accept(server_fd, (sockaddr*)&client_addr, &len);
    if (client_fd == -1) {
        perror("accept failed");
        close(server_fd);
        return -1;
    }

    cout << "客户端已连接: " << inet_ntoa(client_addr.sin_addr) << endl;
// 关闭 Nagle 算法 + 让数据实时到达i
// 获取当前 flag
//int flags = fcntl(client_fd, F_GETFL, 0);
    // 5. 收发数据
    char buf[1024] = {0};//接收的数据
    char buf1[1024]= {0};//截取的有效数据
    char cmd[20]={0};//命令
    int i=0;
    int j=0;
    int k=0;
    char num=0;//计算校验和
    int n;
    //char sql[256];
    while(1){
        //循环前初始化
        memset(buf, 0, sizeof(buf));
        memset(buf1, 0, sizeof(buf1));
        memset(cmd, 0, sizeof(cmd));
        //memset(sql, 0, sizeof(sql));
        i=0;
        j=0;
        num=0;
        n = read(client_fd, buf, sizeof(buf));
        if (n <= 0) {
            cout << "客户端断开" << endl;
            close(client_fd);
            break;
        }
        for(k=0;k<sizeof(buf);k++){
        cout<<buf[k];
        }
        cout<<endl;


        //找$
        while(buf[i]!='$'&&i<1024){
                i++;
        }
        while(buf[i]!='*'&&i<1024){
            buf1[j++]=buf[i++];
        }
        buf1[i]=buf[j];
        buf1[i+1]=buf[j+1];
        i=1;
        //完整语句$和*之间的内容包含$*
        while(buf1[i]!='*'&&i<1024){
            num^=buf1[i++];
        }
        if(num==buf1[++i]){
            //校验和计算成功
            //跳过$
            i=1;
            //截取命令
            while(buf1[i]!=','){
                cmd[i-1]=buf1[i];
                cmd[i]=0;
                i++;
            }


            if(strcmp(cmd, "QUERY_A") == 0){
                //memset(sql, 0, sizeof(sql));
                char sql[256];
                //查询5条信息传回去
                //buf1[i+1]为页数
                j = (buf1[i+1] - 1) * 5;
                sprintf(sql, "SELECT  id, name FROM users ORDER BY id LIMIT %d, 5", j);
                mysql_query(conn, sql);
                MYSQL_RES* res = mysql_store_result(conn);
                if (!res) continue;
                std::string result;
                MYSQL_ROW row;
                // 循环读取每一行数据
                result+="$QUERY_A,";
                while ((row = mysql_fetch_row(res))) {
                        std::string id = row[0] ? row[0] : "";    // 取ID
                        std::string name = row[1] ? row[1] : "";  // 取姓名
                        result += id + "," + name + ",";          // 拼接：ID,姓名,
                }
                result+="*";
                std::cout<<result<<endl;
                strncpy(buf, result.c_str(), sizeof(buf) - 1);
                num =0;
                i=1;
                while(buf[i]!='*'){
                    num^=buf[i++];
                }
                buf[i+1]=num;
                buf[i+2]='\0';
                for(k=0;k<sizeof(buf);k++){
                        cout<<buf[k];
                }
                cout<<endl;
                write(client_fd, buf, strlen(buf));
                mysql_free_result(res);
            }
            else if(strcmp(cmd,"QUERY_B")==0){
                //memset(sql, 0, sizeof(sql));
                //查询5条信息传回去
                //buf1[i+1]为页数
                j = (buf1[i+1] - 1) * 5;
                char sql[256];
                sprintf(sql, "SELECT  log_id, name,check_in_time FROM attendance_logs ORDER BY log_id LIMIT %d, 5", j);
                mysql_query(conn, sql);
                MYSQL_RES* res = mysql_store_result(conn);
                if (!res) continue;
                std::string result;
                MYSQL_ROW row;
                // 循环读取每一行数据
                result+="$QUERY_B,";
                while ((row = mysql_fetch_row(res))) {
                        std::string id = row[0] ? row[0] : "";    // 取ID
                        std::string name = row[1] ? row[1] : "";  // 取姓名
                        std::string time= row[2] ? row[2]: "";       //新增打卡时间
                        result += id + "," + name + ","+time+",";         // 拼接：ID,姓名,
                }
                result+="*";
                std::cout<<result<<endl;
                strncpy(buf, result.c_str(), sizeof(buf) - 1);
                num =0;
                i=1;
                while(buf[i]!='*'){
                    num^=buf[i++];
                }
                buf[i+1]=num;
                buf[i+2]='\0';
                for(k=0;k<sizeof(buf);k++){
                        cout<<buf[k];
                }
                cout<<endl;
                write(client_fd, buf, strlen(buf));
                mysql_free_result(res);
            }
            else if(strcmp(cmd,"DELETE_A")==0){
                //memset(sql, 0, sizeof(sql));
                char sql[256];
                //根据id删除
                //buf1[i+1]为id的起始位置
                memset(cmd, 0, sizeof(cmd));
                j=0;
                while(buf1[i+1]!=','){
                    cmd[j]=buf1[i+1];
                    j++;i++;
                }
                j++;
                cmd[j]='\0';
                sprintf(sql, "DELETE FROM users WHERE id = %s", cmd);

                // 执行删除
                if (mysql_query(conn, sql)) {
                    cout << "删除失败：" << mysql_error(conn) << endl;
                } else {
                    cout << "删除成功！受影响行数：" << (int)mysql_affected_rows(conn) << endl;
                }
            }
            else if(strcmp(cmd,"DELETE_B")==0){
                //memset(sql, 0, sizeof(sql));
                char sql[256];
                //根据id删除
                //buf1[i+1]为log_id的起始位置
                memset(cmd, 0, sizeof(cmd));
                j=0;
                while(buf1[i+1]!=','){
                    cmd[j]=buf1[i+1];
                    j++;i++;
                }
                j++;
                cmd[j]='\0';
                sprintf(sql, "DELETE FROM check_in_time WHERE log_id = %s", cmd);

                // 执行删除
                if (mysql_query(conn, sql)) {
                    cout << "删除失败：" << mysql_error(conn) << endl;
                } else {
                    cout << "check_in_time 中log_id为<<j的信息<<删除成功！受影响行数：" << (int)mysql_affected_rows(conn) << endl;
                }
            }
            else if(strcmp(cmd,"UPDATE_A")==0){
                //memset(sql, 0, sizeof(sql));
                char sql[256];
                //根据id更改
                //buf1[i+1]为id的起始位置
                memset(cmd, 0, sizeof(cmd));
                j=0;
                while(buf1[i+1]!=','){
                    cmd[j]=buf1[i+1];
                    j++;i++;
                }
                j++;
                cmd[j]='\0';
                sprintf(sql, "DELETE FROM users WHERE id = %s", cmd);
               
                sprintf(sql, "SELECT  id, name FROM users ORDER BY id LIMIT %d, 5", j);
                mysql_query(conn, sql);
                MYSQL_RES* res = mysql_store_result(conn);
                if (!res) continue;
                std::string result;
                MYSQL_ROW row;
                // 循环读取每一行数据
                result+="$QUERY_A,";
                while ((row = mysql_fetch_row(res))) {
                        std::string id = row[0] ? row[0] : "";    // 取ID
                        std::string name = row[1] ? row[1] : "";  // 取姓名
                        result += id + "," + name + ",";          // 拼接：ID,姓名,
                }
                result+="*";
                std::cout<<result<<endl;
                strncpy(buf, result.c_str(), sizeof(buf) - 1);
                num =0;
                i=1;
                while(buf[i]!='*'){
                    num^=buf[i++];
                }
                buf[i+1]=num;
                buf[i+2]='\0';
                for(k=0;k<sizeof(buf);k++){
                        cout<<buf[k];
                }
                cout<<endl;
                write(client_fd, buf, strlen(buf));
                mysql_free_result(res);
            }
            else if(strcmp(cmd,"ADD_A")==0){
                 char sql[256];
                // ----------------------
                // 1. 解析 姓名：张三
                // ----------------------
                i++; // 跳过 ,
                char name[64] = {0};
                int name_idx = 0;
                while(buf1[i] != ',' && buf1[i] != '*' && i < 1024) {
                    name[name_idx++] = buf1[i++];
                }

                // ----------------------
                // 2. 解析 label
                // ----------------------
                i++; // 跳过 ,
                char label[64] = {0};
                int label_idx = 0;
                while(buf1[i] != ',' && buf1[i] != '*' && i < 1024) {
                    label[label_idx++] = buf1[i++];
                }

                // ----------------------
                // 3. 插入数据库
                // ----------------------
                // 假设你的表是 users，字段 name, label
                // 如果你的表字段不一样，我可以再改
                sprintf(sql, "INSERT INTO users (name, label) VALUES ('%s', '%s')", name, label);

                if (mysql_query(conn, sql)) {
                    cout << "ADD_A 执行失败：" << mysql_error(conn) << endl;
                } else {
                    cout << "ADD_A 成功 → 姓名：" << name << " 标签：" << label << endl;
                }
            }
            else if(strcmp(cmd,"ADD_B")==0){
                // ======================
                // 1. 解析 label
                // ======================
                i++; // 跳过 ,
                char label_str[32] = {0};
                int idx = 0;
                while(buf1[i] != '*' && i < 1024 && idx < 31) {
                    label_str[idx++] = buf1[i++];
                }
                int label = atoi(label_str);

                // ======================
                // 2. 根据 label 查询 users 表
                // ======================
                char sql_sel[256];
                sprintf(sql_sel, "SELECT id, name FROM users WHERE label = %d", label);
                if (mysql_query(conn, sql_sel)) {
                    cout << "查询users失败：" << mysql_error(conn) << endl;
                    break;
                }

                MYSQL_RES* res = mysql_store_result(conn);
                if (!res || mysql_num_rows(res) == 0) {
                    cout << "无此label：" << label << endl;
                    if (res) mysql_free_result(res);
                    break;
                }

                 MYSQL_ROW row = mysql_fetch_row(res);
                char user_id[32]  = {0};
                char user_name[64] = {0};
                if (row[0]) strcpy(user_id, row[0]);
                if (row[1]) strcpy(user_name, row[1]);
                mysql_free_result(res);

                // ======================
                // 3. 插入考勤表：attendance_logs
                // ======================
                char sql_ins[256];
                sprintf(sql_ins,
                    "INSERT INTO attendance_logs (user_id, name, check_in_time, location, confidence) "
                    "VALUES ('%s', '%s', NOW(), '打卡机1', '91')",
                    user_id, user_name);

                if (mysql_query(conn, sql_ins)) {
                    cout << "打卡记录插入失败：" << mysql_error(conn) << endl;
                } else {
                    cout << "✅ 打卡成功 → 姓名：" << user_name << " user_id：" << user_id << endl;
                }               
            }
        }




       
    }
    }
    // 6. 关闭
    close(server_fd);
    mysql_close(conn);

    return 0;

}