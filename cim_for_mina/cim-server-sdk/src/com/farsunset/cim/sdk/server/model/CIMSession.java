/**
 * Copyright 2013-2023 Xia Jun(3979434@qq.com).
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 ***************************************************************************************
 *                                                                                     *
 *                        Website : http://www.farsunset.com                           *
 *                                                                                     *
 ***************************************************************************************
 */
package com.farsunset.cim.sdk.server.model;

import java.io.Serializable;
import java.net.SocketAddress;
import java.util.Objects;

import org.apache.mina.core.future.WriteFuture;
import org.apache.mina.core.session.IoSession;

import com.farsunset.cim.sdk.server.constant.CIMConstant;
import com.farsunset.cim.sdk.server.model.proto.SessionProto;
import com.google.protobuf.InvalidProtocolBufferException;

/**
 * IoSession包装类,集群时 将此对象存入表中
 */
public class CIMSession implements Serializable {

	private transient static final long serialVersionUID = 1L;

	public transient static String PROTOCOL = "protocol";
	public transient static String WEBSOCKET = "websocket";
	public transient static String NATIVEAPP = "nativeapp";

	public transient static String HOST = "HOST";
	public transient static final int STATE_ENABLED = 0;
	public transient static final int STATE_DISABLED = 1;
	public transient static final int APNS_ON = 1;
	public transient static final int APNS_OFF = 0;

	public transient static String CHANNEL_IOS = "ios";
	public transient static String CHANNEL_ANDROID = "android";
	public transient static String CHANNEL_WINDOWS = "windows";
	public transient static String CHANNEL_WP = "wp";
	public transient static String CHANNEL_BROWSER = "browser";

	private transient IoSession session;

	private String account;// session绑定的账号,主键，一个账号同一时间之内在一个设备在线
	private Long nid;// session在本台服务器上的ID
	private String deviceId;// 客户端ID (设备号码+应用包名),ios为devicetoken
	private String host;// session绑定的服务器IP
	private String channel;// 终端设备类型
	private String deviceModel;// 终端设备型号
	private String clientVersion;// 终端应用版本
	private String systemVersion;// 终端系统版本
	private Long bindTime;// 登录时间
	private Double longitude;// 经度
	private Double latitude;// 维度
	private String location;// 位置
	private int apns;// apns推送状态
	private int state;// 状态

	public CIMSession(IoSession session) {
		this.session = session;
		this.nid = session.getId();
	}

	public CIMSession() {

	}

	public String getAccount() {
		return account;
	}

	public void setAccount(String account) {
		this.account = account;

		setAttribute(CIMConstant.KEY_ACCOUNT, account);
	}

	public Double getLongitude() {
		return longitude;
	}

	public void setLongitude(Double longitude) {
		this.longitude = longitude;
	}

	public Double getLatitude() {
		return latitude;
	}

	public void setLatitude(Double latitude) {
		this.latitude = latitude;
	}

	public String getLocation() {
		return location;
	}

	public void setLocation(String location) {
		this.location = location;
	}

	public Long getNid() {
		return nid;
	}

	public void setNid(Long nid) {
		this.nid = nid;
	}

	public String getDeviceId() {
		return deviceId;
	}

	public String getChannel() {
		return channel;
	}

	public void setChannel(String channel) {
		this.channel = channel;
	}

	public String getDeviceModel() {
		return deviceModel;
	}

	public void setDeviceModel(String deviceModel) {
		this.deviceModel = deviceModel;
	}

	public void setDeviceId(String deviceId) {
		this.deviceId = deviceId;
	}

	public String getHost() {
		return host;
	}

	public Long getBindTime() {
		return bindTime;
	}

	public void setBindTime(Long bindTime) {
		this.bindTime = bindTime;
	}

	public String getClientVersion() {
		return clientVersion;
	}

	public void setClientVersion(String clientVersion) {
		this.clientVersion = clientVersion;
	}

	public String getSystemVersion() {
		return systemVersion;
	}

	public void setSystemVersion(String systemVersion) {
		this.systemVersion = systemVersion;
	}

	public void setHost(String host) {
		this.host = host;
	}

	public int getApns() {
		return apns;
	}

	public void setApns(int apns) {
		this.apns = apns;
	}

	public int getState() {
		return state;
	}

	public void setState(int state) {
		this.state = state;
	}

	public void setAttribute(String key, Object value) {
		if (session != null)
			session.setAttribute(key, value);
	}

	public boolean containsAttribute(String key) {
		if (session != null)
			return session.containsAttribute(key);
		return false;
	}

	public Object getAttribute(String key) {
		if (session != null)
			return session.getAttribute(key);
		return null;
	}

	public void removeAttribute(String key) {
		if (session != null)
			session.removeAttribute(key);
	}

	public SocketAddress getRemoteAddress() {
		if (session != null)
			return session.getRemoteAddress();
		return null;
	}

	public boolean write(Object msg) {
		if (session != null) {
			WriteFuture future = session.write(msg);
			future.awaitUninterruptibly(10 * 1000);
			return future.isWritten();
		}

		return false;
	}

	public boolean isConnected() {
		return (session != null && session.isConnected()) || state == STATE_ENABLED;
	}

	public void closeNow() {
		if (session != null)
			session.closeNow();
	}

	public void closeOnFlush() {
		if (session != null)
			session.closeOnFlush();
	}

	public boolean isIOSChannel() {
		return Objects.equals(channel, CHANNEL_IOS);
	}

	public boolean isAndroidChannel() {
		return Objects.equals(channel, CHANNEL_ANDROID);
	}

	public boolean isWindowsChannel() {
		return Objects.equals(channel, CHANNEL_WINDOWS);
	}

	public boolean isApnsOpend() {
		return Objects.equals(apns, APNS_ON);
	}

	@Override
	public int hashCode() {
		return getClass().hashCode();
	}

	@Override
	public boolean equals(Object o) {
		if (o instanceof CIMSession) {
			CIMSession target = (CIMSession) o;
			return Objects.equals(target.deviceId, deviceId) && Objects.equals(target.nid, nid)
					&& Objects.equals(target.host, host);
		}
		return false;
	}

	public IoSession getSession() {
		return session;
	}

	public void setSession(IoSession session) {
		this.session = session;
	}

	public byte[] getProtobufBody() {
		SessionProto.Model.Builder builder = SessionProto.Model.newBuilder();
		if (account != null) {
			builder.setAccount(account);
		}
		if (nid != null) {
			builder.setNid(nid);
		}
		if (deviceId != null) {
			builder.setDeviceId(deviceId);
		}
		if (host != null) {
			builder.setHost(host);
		}
		if (channel != null) {
			builder.setChannel(channel);
		}
		if (deviceModel != null) {
			builder.setDeviceModel(deviceModel);
		}
		if (clientVersion != null) {
			builder.setClientVersion(clientVersion);
		}
		if (systemVersion != null) {
			builder.setSystemVersion(systemVersion);
		}
		if (bindTime != null) {
			builder.setBindTime(bindTime);
		}
		if (longitude != null) {
			builder.setLongitude(longitude);
		}
		if (latitude != null) {
			builder.setLatitude(latitude);
		}
		if (location != null) {
			builder.setLocation(location);
		}
		builder.setState(state);
		builder.setApns(apns);
		return builder.build().toByteArray();
	}
	
	
	public static CIMSession decode(byte[] protobufBody) throws InvalidProtocolBufferException {
		if(protobufBody == null) {
			return null;
		}
		SessionProto.Model proto = SessionProto.Model.parseFrom(protobufBody);
		CIMSession session = new CIMSession();
		session.setApns(proto.getApns());
		session.setBindTime(proto.getBindTime());
		session.setChannel(proto.getChannel());
		session.setClientVersion(proto.getClientVersion());
		session.setDeviceId(proto.getDeviceId());
		session.setDeviceModel(proto.getDeviceModel());
		session.setHost(proto.getHost());
		session.setLatitude(proto.getLatitude());
		session.setLongitude(proto.getLongitude());
		session.setLocation(proto.getLocation());
		session.setNid(proto.getNid());
		session.setSystemVersion(proto.getSystemVersion());
		session.setState(proto.getState());
		session.setAccount(proto.getAccount());
		return session;
	}
	

}
