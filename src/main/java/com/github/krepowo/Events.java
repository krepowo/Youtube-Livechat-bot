package com.github.krepowo;

import com.github.krepowo.Util.PropertyManager;
import com.github.kusaanko.youtubelivechat.ChatItem;
import com.github.kusaanko.youtubelivechat.IdType;
import com.github.kusaanko.youtubelivechat.YouTubeLiveChat;
import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.Activity;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.events.ReadyEvent;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.jetbrains.annotations.NotNull;

import java.awt.*;
import java.io.IOException;

public class Events extends ListenerAdapter {

    @Override
    public void onMessageReceived(@NotNull MessageReceivedEvent event) {
        Message msg = event.getMessage();

        if(msg.getContentRaw().toLowerCase().startsWith("@everyone")) {
            String asaltag = null;
            String ytChannel = null;
            String tujuanEmbed = null;
            String pingCord = null;
            try {
                asaltag = new PropertyManager().GetProp("idChanEveryone");
                ytChannel = new PropertyManager().getProp("chanURL");
                tujuanEmbed = new PropertyManager().getProp("idChanSend");
                pingCord = new PropertyManager().getProp("pingCordID");
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
            if(!msg.getAuthor().getId().equals(pingCord)) return;

            if(!event.getChannel().getId().equals(asaltag)) return;

                try {
                    String chanID = YouTubeLiveChat.getChannelIdFromURL(ytChannel);
                    YouTubeLiveChat chat = new YouTubeLiveChat(chanID, true, IdType.CHANNEL);

                    event.getGuild().getTextChannelById(tujuanEmbed).sendMessageFormat("Starting livechat with stream ID: %s", chat.getVideoId()).queue();

                    int liveStatusCheckCycle = 0;
                    while (true) {
                        chat.update();
                        for (ChatItem item : chat.getChatItems()) {
                            EmbedBuilder embed = new EmbedBuilder().setAuthor(item.getAuthorName(), "https://www.youtube.com/channel/"+item.getAuthorChannelID(), item.getAuthorIconURL()).setTimestamp(null);

                            if(item.isAuthorModerator()) {
                                embed.setColor(Color.BLUE);
                                embed.setFooter("Chat Moderator", "https://images-ext-2.discordapp.net/external/BaPwAwAOi_956J4mhAf1ZFJNDM3JHJszdoRLPvxqsvQ/https/cdn.discordapp.com/emojis/805643675359117352.png");
                            } else if(item.isAuthorOwner()) {
                                embed.setColor(Color.yellow);
                                embed.setFooter("Channel Owner", "https://images-ext-1.discordapp.net/external/ZkQRlQO7AajT_90yk6XuZd5ws1fbxKASuhnAfe4aNsM/https/yt3.ggpht.com/98uz9ud0Ho9ylf4eYTo5wxGSqXf0fUAbhrDVngjQN1pY5RlKbmF1SE4tXr4PE5i05MjM3-sLzYs%3Ds800-c-k-c0x00ffffff-no-rj");
                            } else if(item.isAuthorVerified()){
                                embed.setColor(Color.CYAN);
                                embed.setFooter("Verified User", "https://images-ext-2.discordapp.net/external/BaPwAwAOi_956J4mhAf1ZFJNDM3JHJszdoRLPvxqsvQ/https/cdn.discordapp.com/emojis/805643675359117352.png");
                            } else {
                                embed.setColor(Color.green);
                                embed.setFooter("Normal User", "https://images-ext-2.discordapp.net/external/BaPwAwAOi_956J4mhAf1ZFJNDM3JHJszdoRLPvxqsvQ/https/cdn.discordapp.com/emojis/805643675359117352.png");
                            }

                            embed.setDescription(item.getMessage());
                            event.getGuild().getTextChannelById(tujuanEmbed).sendMessageEmbeds(embed.build()).queue();

                            liveStatusCheckCycle++;
                            if(liveStatusCheckCycle >= 10) {
                                if(chat.getBroadcastInfo().isLiveNow.equals(false)) {
                                    System.out.println("Live Stopped");
                                    break;
                                }
                                liveStatusCheckCycle = 0;
                            }
                        }

                        try {
                            Thread.sleep(1000);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
        }
    }

    @Override
    public void onReady(@NotNull ReadyEvent event) {
        event.getJDA().getPresence().setActivity(Activity.watching("Youtube Live"));
    }
}
