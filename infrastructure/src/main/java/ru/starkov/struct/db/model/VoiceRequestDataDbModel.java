package ru.starkov.struct.db.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.Column;
import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import jakarta.persistence.Transient;
import lombok.Getter;
import lombok.Setter;

@Entity
@DiscriminatorValue("voice")
@Getter
@Setter
public class VoiceRequestDataDbModel extends RequestDataDbModel<byte[]> {

    @Column(name = "byte_data")
    @JsonIgnore
    private byte[] data;

    @Column(name = "recognized_voice_request", length = 4000)
    private String recognizedVoiceRequest;

    @Column(name = "voice_request_duration")
    private Integer voiceRequestDuration;

    @Override
    public byte[] getData() {
        return this.data;
    }

    @Override
    public void setData(byte[] data) {
        this.data = data;
    }

    @Transient
    public int getDataSize() {
        return (data != null) ? data.length : 0;
    }

}
