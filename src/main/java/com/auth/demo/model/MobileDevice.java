package com.auth.demo.model;

import jakarta.persistence.*;

import java.util.Objects;

@Entity
@Table(name = "mobile_device")
public class MobileDevice extends BaseEntity {

    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "mobile_device_seq")
    @SequenceGenerator(name = "mobile_device_seq", allocationSize = 1)
    private Long id;

    @Column(name = "device_id", length = 255, nullable = false)
    private String deviceId;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Enumerated(EnumType.STRING)
    @Column(name = "device_type", nullable = false)
    private DeviceType deviceType;

    public MobileDevice() {

    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getId() {
        return id;
    }

    public String getDeviceId() {
        return deviceId;
    }

    public void setDeviceId(String deviceId) {
        this.deviceId = deviceId;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public DeviceType getDeviceType() {
        return deviceType;
    }

    public void setDeviceType(DeviceType deviceType) {
        this.deviceType = deviceType;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        MobileDevice that = (MobileDevice) o;

        if (!Objects.equals(deviceId, that.deviceId)) return false;
        if (!Objects.equals(user, that.user)) return false;
        if (!Objects.equals(deviceType, that.deviceType)) return false;
        return id != null && Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        int result = id != null ? id.hashCode() : 0;
        result = 31 * result + (deviceId != null ? deviceId.hashCode() : 0);
        result = 31 * result + (user != null ? user.hashCode() : 0);
        result = 31 * result + (deviceType != null ? deviceType.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "MobileDevice{" +
                "id=" + id +
                ", deviceId='" + deviceId + '\'' +
                ", deviceType=" + deviceType +
                ", createdAt=" + createdAt +
                ", updatedAt=" + updatedAt +
                '}';
    }
}
