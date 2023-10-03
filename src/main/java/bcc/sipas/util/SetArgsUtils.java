package bcc.sipas.util;

import io.lettuce.core.SetArgs;
import io.lettuce.core.codec.RedisCodec;
import io.lettuce.core.protocol.CommandArgs;

import java.time.Duration;

public class SetArgsUtils {

    public static SetArgs buildMinute(int number){
        return SetArgs
                .Builder
                .ex(Duration.ofMinutes(number));
    }

    public static SetArgs buildSecond(int number){
        return SetArgs
                .Builder
                .ex(Duration.ofSeconds(number));
    }
}
