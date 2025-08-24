package com.solicare.app.backend.domain.dto.output.push;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor(staticName = "of")
public class FcmSendOutputDetail implements PushSendOutput.Detail {
  private Status status;
  private Exception exception;

  public boolean isSuccess() {
    return status == Status.SENT;
  }

  public enum Status {
    SENT,
    UNAVAILABLE,
    ERROR
  }
}
