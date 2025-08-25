package com.solicare.app.backend.domain.dto.output.push;

import com.solicare.app.backend.domain.dto.output.OperationOutput;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.List;

@Getter
@RequiredArgsConstructor(staticName = "of")
@AllArgsConstructor(staticName = "of")
public class PushSendOutput<DetailClassType extends PushSendOutput.Detail>
        implements OperationOutput {
    private final List<DetailClassType> details;
    private Status status;

    public PushSendOutput<DetailClassType> setStatusByDetails() {
        int targetCount = getTargetCount();
        int successCount = getSuccessCount();

        if (targetCount == 0) {
            status = Status.NON_SENT;
        } else if (successCount == targetCount) {
            status = Status.ALL_SENT;
        } else if (successCount > 0) {
            status = Status.PARTIALLY_SENT;
        } else {
            status = Status.NON_SENT;
        }
        return this;
    }

    public int getTargetCount() {
        return (details == null) ? 0 : details.size();
    }

    public int getSuccessCount() {
        return (details == null) ? 0 : details.stream().filter(Detail::isSuccess).toList().size();
    }

    @Override
    public boolean isSuccess() {
        return status == Status.ALL_SENT || status == Status.PARTIALLY_SENT;
    }

    public enum Status {
        MEMBER_NOT_FOUND,
        ALL_SENT,
        PARTIALLY_SENT,
        NON_SENT
    }

    public interface Detail {
        boolean isSuccess();

        Exception getException();
    }
}
