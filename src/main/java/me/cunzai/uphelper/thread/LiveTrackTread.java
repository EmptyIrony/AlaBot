package me.cunzai.uphelper.thread;

import com.google.gson.JsonObject;
import lombok.SneakyThrows;
import me.cunzai.uphelper.UpHelper;
import me.cunzai.uphelper.util.JsonUtil;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.concurrent.FutureCallback;

import java.io.*;

import static java.lang.Thread.sleep;

public class LiveTrackTread implements Runnable{
    private boolean isLiving;
    private String title;
    private String url;
    private String newVideoTitle;
    private long newVideoTime;
    private String av;
    private String bv;

    public LiveTrackTread(){
        load();
    }


    @SneakyThrows
    public void run() {
        System.out.println("开始监听");
        while (true){
            try{
                UpHelper.getInstance().getHttpAsyncClient()
                        .execute(new HttpGet("https://api.live.bilibili.com/room/v1/Room/getRoomInfoOld?mid=14890801"),
                                new FutureCallback<HttpResponse>() {
                                    @Override
                                    public void completed(HttpResponse httpResponse) {
                                        load();

                                        JsonObject json = JsonUtil.getJsonFromResponse(httpResponse);
                                        json = json.get("data").getAsJsonObject();
                                        boolean isLiving = json.get("liveStatus").getAsInt() == 1;
                                        if (isLiving && !LiveTrackTread.this.isLiving){
                                            LiveTrackTread.this.isLiving = true;
                                            LiveTrackTread.this.title = json.get("title").getAsString();
                                            LiveTrackTread.this.url = json.get("url").getAsString();

                                            startLiving();
                                        }
                                        if (!isLiving && LiveTrackTread.this.isLiving){
                                            LiveTrackTread.this.isLiving = false;

                                            stopLiving();
                                        }
                                        save();

                                    }

                                    @Override
                                    public void failed(Exception e) {
                                        e.printStackTrace();
                                    }

                                    @Override
                                    public void cancelled() {

                                    }
                                });

                UpHelper.getInstance()
                        .getHttpAsyncClient()
                        .execute(new HttpGet("https://api.bilibili.com/x/space/arc/search?mid=14890801&pn=1&ps=1")
                                , new FutureCallback<HttpResponse>() {
                                    @Override
                                    public void completed(HttpResponse response) {
                                        load();
                                        JsonObject jsonObject = JsonUtil.getJsonFromResponse(response);
                                        JsonObject json = jsonObject.getAsJsonObject("data").getAsJsonObject("list").getAsJsonArray("vlist")
                                                .get(0).getAsJsonObject();

                                        long created = json.get("created").getAsLong();
                                        if (created > LiveTrackTread.this.newVideoTime){
                                            LiveTrackTread.this.newVideoTime = created;
                                            LiveTrackTread.this.newVideoTitle = json.get("title").getAsString();
                                            LiveTrackTread.this.av = json.get("aid").getAsString();
                                            LiveTrackTread.this.bv = json.get("bvid").getAsString();

                                            newVideo();
                                        }

                                        save();
                                    }

                                    @Override
                                    public void failed(Exception e) {
                                        e.printStackTrace();
                                    }

                                    @Override
                                    public void cancelled() {

                                    }
                                });
            }catch (Exception e){
                e.printStackTrace();
            }finally {
                save();
                sleep(1000*10);
            }


        }
    }

    private void newVideo(){
        UpHelper.getInstance()
                .getCoolQ()
                .sendGroupMsg(480867115)
                .append(UpHelper.getInstance().getCQCode().at(-1))
                .append("\r\n主播更新新视频了\r\n")
                .append("视频标题: ")
                .append(this.newVideoTitle)
                .append("\r\nAV号: AV")
                .append(this.av)
                .append("\r\nBV号: ")
                .append(this.bv)
                .append("\r\n视频链接: ")
                .append("\r\nhttps://www.bilibili.com/video/av")
                .append(this.av)
                .append("\r\n快去给主播三连吧")
                .send();
    }

    private void startLiving(){
        UpHelper.getInstance()
                .getCoolQ()
                .sendGroupMsg(480867115)
                .append(UpHelper.getInstance().getCQCode().at(-1))
                .append("\r\n主播直播了\r\n")
                .append("直播标题: ")
                .append(this.title)
                .append("\r\n直播链接: \r\n")
                .append(this.url)
                .send();

    }

    private void stopLiving(){
        UpHelper.getInstance()
                .getCoolQ()
                .sendGroupMsg(480867115)
                .append("主播下播了")
                .send();
    }

    @SneakyThrows
    private void save(){
        String serialize = serialize();
        File file = new File(UpHelper.getInstance().appDirectory + "\\LivingStatus");
        FileWriter fileWriter = new FileWriter(file);
        fileWriter.write(serialize);
        fileWriter.flush();
    }
    @SneakyThrows
    private void load(){
        File file = new File(UpHelper.getInstance().appDirectory + "\\LivingStatus");
        if (!file.exists()){
            UpHelper.getInstance().getCoolQ().logDebug("file","文件不存在");
            return;
        }
        FileInputStream inputStream = new FileInputStream(file);
        InputStreamReader inputStreamReader = new InputStreamReader(inputStream);
        BufferedReader reader = new BufferedReader(inputStreamReader);
        JsonObject json = JsonUtil.getJsonFromString(reader.readLine());
        
        this.isLiving = json.get("living").getAsBoolean();
        this.newVideoTime = json.get("newVideoTime").getAsLong();
    }

    private String serialize(){
        JsonObject json = new JsonObject();
        json.addProperty("living",this.isLiving);
        json.addProperty("newVideoTime",this.newVideoTime);
        return json.toString();
    }

    private JsonObject deserialization(String str){
        return JsonUtil.getJsonFromString(str);
    }
}
