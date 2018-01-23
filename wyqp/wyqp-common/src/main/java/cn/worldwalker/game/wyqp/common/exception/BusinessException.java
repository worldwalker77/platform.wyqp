package cn.worldwalker.game.wyqp.common.exception;


/**
 * 异常父类，所有业务异常继承该类
 *
 */
public class BusinessException extends RuntimeException {

    private static final long serialVersionUID = 6511369321028049305L;

    /**
     * 异常代码
     */
    protected Integer          bussinessCode;
    /**
    /**
     * 异常信息描述
     */
    protected String          message;

    
    public BusinessException(String message) {
        this.message = message;
    }

    public BusinessException(Integer bussinessCode, String message) {
        this.bussinessCode = bussinessCode;
        this.message = message;
    }
    
    public BusinessException(ExceptionEnum exceptionEnum) {
        if(exceptionEnum!=null){
            this.bussinessCode = exceptionEnum.index;
            this.message = exceptionEnum.description;
        }
    }
    

    public Integer getBussinessCode() {
		return bussinessCode;
	}

	public void setBussinessCode(Integer bussinessCode) {
		this.bussinessCode = bussinessCode;
	}

	public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
