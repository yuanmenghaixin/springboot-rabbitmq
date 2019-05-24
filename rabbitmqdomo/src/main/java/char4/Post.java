package char4;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import net.sf.json.JSONObject;
public class Post {
    /**
     * 定义所需的变量
     */
    private static HttpClient httpClient = new DefaultHttpClient();
    private static HttpPost httppost;
    private static HttpResponse response;
    private HttpEntity entity;
    private String postResult = null;
    public static void main(String[] args) {
        String loginURL = "http://172.22.123.16:8080/getPDXFileUrl";//请求地址如上
        // 创建一个httppost请求
        httppost = new HttpPost(loginURL);
        JSONObject jsonParam = new JSONObject();
        jsonParam.put("Appkey", "UNOZWjGbfZ5S");
        jsonParam.put("Diag_SN","e10adc3949ba59abbe56e057f20f883e");
        jsonParam.put("Mac_Addr","e10adc3949ba59abbe56e057f20f883e");
        jsonParam.put("F100","0406010200");
        jsonParam.put("Vin","12312311111122222");
        jsonParam.put("Time","2019-04-23 16:45");
        try {
            StringEntity entity = new StringEntity(jsonParam.toString(), "utf-8");// 解决中文乱码问题
            entity.setContentEncoding("UTF-8");
            entity.setContentType("application/json");
            httppost.setEntity(entity);
            response = httpClient.execute(httppost);
            String strResult = EntityUtils.toString(response.getEntity());
            System.out.println("查看返回的结果：" + strResult);
        } catch (Exception e) {
            e.printStackTrace();
        }
        httppost.releaseConnection();
    }
}