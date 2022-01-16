package com.reactnativearmfitsdk

import android.os.Parcel
import android.os.Parcelable
import com.reactnativearmfitsdk.utils.ByteUtils
import com.reactnativearmfitsdk.utils.toUInt
import java.util.*
object Er1BleResponse {
  class Er1Response constructor(var bytes: ByteArray) : Parcelable {
    var cmd: Int
    var pkgType: Byte
    var pkgNo: Int
    var len: Int
    var content: ByteArray

    constructor(parcel: Parcel) : this(parcel.createByteArray()!!) {
      cmd = parcel.readInt()
      pkgType = parcel.readByte()
      pkgNo = parcel.readInt()
      len = parcel.readInt()
      content = parcel.createByteArray()!!
    }

    init {
      cmd = (bytes[1].toUInt() and 0xFFu).toInt()
      pkgType = bytes[3]
      pkgNo = (bytes[4].toUInt() and 0xFFu).toInt()
      len = toUInt(bytes.copyOfRange(5, 7))
      content = bytes.copyOfRange(7, 7+len)
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
      parcel.writeByteArray(bytes)
      parcel.writeInt(cmd)
      parcel.writeByte(pkgType)
      parcel.writeInt(pkgNo)
      parcel.writeInt(len)
      parcel.writeByteArray(content)
    }

    override fun describeContents(): Int {
      return 0
    }

    companion object CREATOR : Parcelable.Creator<Er1Response> {
      override fun createFromParcel(parcel: Parcel): Er1Response {
        return Er1Response(parcel)
      }

      override fun newArray(size: Int): Array<Er1Response?> {
        return arrayOfNulls(size)
      }
    }
  }


  class RtData constructor(var bytes: ByteArray) : Parcelable {
    var content: ByteArray = bytes
    var param: RtParam
    var wave: RtWave

    constructor(parcel: Parcel) : this(parcel.createByteArray()!!) {
      content = parcel.createByteArray()!!
      param = parcel.readParcelable(RtParam::class.java.classLoader)!!
      wave = parcel.readParcelable(RtWave::class.java.classLoader)!!
    }

    init {
//            LogUtils.d(bytes.toHex())
      param = RtParam(bytes.copyOfRange(0, 20))
      wave = RtWave(bytes.copyOfRange(20, bytes.size))
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
      parcel.writeByteArray(bytes)
      parcel.writeByteArray(content)
      parcel.writeParcelable(param, flags)
      parcel.writeParcelable(wave, flags)
    }

    override fun describeContents(): Int {
      return 0
    }

    companion object CREATOR : Parcelable.Creator<RtData> {
      override fun createFromParcel(parcel: Parcel): RtData {
        return RtData(parcel)
      }

      override fun newArray(size: Int): Array<RtData?> {
        return arrayOfNulls(size)
      }
    }
  }

  class RtParam constructor(var bytes: ByteArray) : Parcelable {
    var hr: Int
    var sysFlag: Byte
    var battery: Int
    var recordTime: Int = 0
    var runStatusByte: Byte
    var status: RunStatus
    var leadOn: Boolean

    constructor(parcel: Parcel) : this(parcel.createByteArray()!!) {
      hr = parcel.readInt()
      sysFlag = parcel.readByte()
      battery = parcel.readInt()
      recordTime = parcel.readInt()
      runStatusByte = parcel.readByte()
      leadOn = parcel.readByte() != 0.toByte()
    }
    // reserve 11

    init {
//            LogUtils.d(bytes.toHex())
      hr = toUInt(bytes.copyOfRange(0, 2))
      sysFlag = bytes[2]
      battery = (bytes[3].toUInt() and 0xFFu).toInt()
      if (bytes[8].toUInt() and 0x02u == 0x02u) {
        recordTime = toUInt(bytes.copyOfRange(4, 8))
      }
      runStatusByte = bytes[8]
      status = RunStatus(runStatusByte)
      leadOn = (bytes[8].toUInt() and 0x07u) != 0x07u
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
      parcel.writeByteArray(bytes)
      parcel.writeInt(hr)
      parcel.writeByte(sysFlag)
      parcel.writeInt(battery)
      parcel.writeInt(recordTime)
      parcel.writeByte(runStatusByte)
      parcel.writeByte(if (leadOn) 1 else 0)
    }

    override fun describeContents(): Int {
      return 0
    }

    companion object CREATOR : Parcelable.Creator<RtParam> {
      override fun createFromParcel(parcel: Parcel): RtParam {
        return RtParam(parcel)
      }

      override fun newArray(size: Int): Array<RtParam?> {
        return arrayOfNulls(size)
      }
    }
  }


  class RtWave constructor(var bytes: ByteArray) : Parcelable {
    var content: ByteArray = bytes
    var len: Int
    var wave: ByteArray
    var wFs : FloatArray? = null

    constructor(parcel: Parcel) : this(parcel.createByteArray()!!) {
      content = parcel.createByteArray()!!
      len = parcel.readInt()
      wave = parcel.createByteArray()!!
      wFs = parcel.createFloatArray()
    }

    init {
//            LogUtils.d(bytes.toHex())
      len = toUInt(bytes.copyOfRange(0, 2))
      wave = bytes.copyOfRange(2, bytes.size)
      wFs = FloatArray(len)
      for (i in 0 until len) {
        wFs!![i] = Er1DataController.byteTomV(wave[2 * i], wave[2 * i + 1])
      }
//            LogUtils.d(Arrays.toString(wFs))
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
      parcel.writeByteArray(bytes)
      parcel.writeByteArray(content)
      parcel.writeInt(len)
      parcel.writeByteArray(wave)
      parcel.writeFloatArray(wFs)
    }

    override fun describeContents(): Int {
      return 0
    }

    companion object CREATOR : Parcelable.Creator<RtWave> {
      override fun createFromParcel(parcel: Parcel): RtWave {
        return RtWave(parcel)
      }

      override fun newArray(size: Int): Array<RtWave?> {
        return arrayOfNulls(size)
      }
    }
  }


  class RtRriData constructor(var bytes: ByteArray) : Parcelable {
    var content: ByteArray = bytes
    var param: RtRriParam
    var rri: RtRri

    constructor(parcel: Parcel) : this(parcel.createByteArray()!!) {
      content = parcel.createByteArray()!!
    }

    init {
//            LogUtils.d(bytes.toHex())
      param = RtRriParam(bytes.copyOfRange(0, 21))
      rri = RtRri(bytes.copyOfRange(21, bytes.size), param.unix_time)
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
      parcel.writeByteArray(bytes)
      parcel.writeByteArray(content)
    }

    override fun describeContents(): Int {
      return 0
    }

    companion object CREATOR : Parcelable.Creator<RtRriData> {
      override fun createFromParcel(parcel: Parcel): RtRriData {
        return RtRriData(parcel)
      }

      override fun newArray(size: Int): Array<RtRriData?> {
        return arrayOfNulls(size)
      }
    }
  }


  class RtRriParam constructor(var bytes: ByteArray) {
    var hr: Int
    var sysFlag: Byte
    var battery: Int
    var recordTime: Int = 0
    var runStatusByte: Byte
    var status: RunStatus
    var leadOn: Boolean

    // rri version
    var ms: Int
    var s: Int

    /**
     * 三轴加速度 3-axis acceleration
     * 单位：千分之一重力加速度
     */
    var axis_x: Float
    var axis_y: Float
    var axis_z: Float
    var unix_time: Long

    init {
//            LogUtils.d(bytes.toHex())
      var index = 0
      hr = toUInt(bytes.copyOfRange(index, index + 2))
      index += 2
      sysFlag = bytes[index]
      index++
      battery = (bytes[index].toUInt() and 0xFFu).toInt()
      index++
      if (bytes[8].toUInt() and 0x02u == 0x02u) {
        recordTime = toUInt(bytes.copyOfRange(index, index+4))
      }
      index+=4
      runStatusByte = bytes[8]
      status = RunStatus(runStatusByte)
      leadOn = (bytes[8].toUInt() and 0x07u) != 0x07u
      index++
      s = toUInt(bytes.copyOfRange(index, index+4))
      index+=4
      ms = toUInt(bytes.copyOfRange(index, index+2))
      index+=2
      axis_x = ByteUtils.bytes2Short(bytes[index], bytes[index+1]).toFloat() * 2000 /32768
      index+=2
      axis_y = ByteUtils.bytes2Short(bytes[index], bytes[index+1]).toFloat() * 2000 /32768
      index+=2
      axis_z = ByteUtils.bytes2Short(bytes[index], bytes[index+1]).toFloat() * 2000 /32768
      index+=2

      unix_time = s.toLong()*1000+ms

      val c = Calendar.getInstance()
      c.timeInMillis = unix_time
//            LogUtils.d("$s + $ms = $unix_time", c.toString(), "($axis_x, $axis_y, $axis_z)")
    }
  }


  class RtRri constructor(var bytes: ByteArray, var end: Long) {
    var content: ByteArray = bytes
    var len: Int
    var wave: ByteArray
    var rris = mutableListOf<RRI>()

    init {
//            LogUtils.d(bytes.toHex())
      len = toUInt(bytes.copyOfRange(0, 2))
      wave = bytes.copyOfRange(2, bytes.size)
      for (i in 0 until len) {
        rris.add(RRI(end - (len-i)*4, toUInt(wave.copyOfRange(2*i, 2*i+2))))
      }
//            LogUtils.d(rris.toString())
    }
  }

  class RRI constructor(var time: Long, var value: Int) {
    override fun toString(): String {
      return "$time:$value\n"
    }
  }

  class Er3RtData constructor(var bytes: ByteArray) {
    var content: ByteArray = bytes
    var param: RtParam
    var wave: Er3RtWave

    init {
      param = RtParam(bytes.copyOfRange(0, 20))
      wave = Er3RtWave(bytes.copyOfRange(20, bytes.size))
    }
  }

  class Er3RtWave constructor(var bytes: ByteArray) {
    var content: ByteArray = bytes
    var len: Int
    val channels = 8
    var wave : ByteArray? = null
    var waveFs : FloatArray? = null

    init {
      len = toUInt(bytes.copyOfRange(0, 2))
      if (len > 0) {
        wave = bytes.copyOfRange(2, bytes.size)

        waveFs = FloatArray(len * channels)

        for (i in 0 until (len * channels)) {
          waveFs!![i] = Er1DataController.byteTomV(wave!![2 * i], wave!![2 * i + 1])
        }
//                LogUtils.d(wave!!.toHex())
//                LogUtils.d(Arrays.toString(waveFs))
      }
    }
  }


  class Er1File(val name:String, val size: Int) {
    var fileName: String
    var fileSize: Int
    var content: ByteArray
    var index: Int // 标识当前下载index

    init {
      fileName = name
      fileSize = size
      content = ByteArray(size)
      index = 0
    }

    fun addContent(bytes: ByteArray) {
      if (index >= fileSize) {
        return // 已下载完成
      } else {
        System.arraycopy(bytes, 0, content, index, bytes.size)
        index += bytes.size
      }
    }
  }


  class Er1FileList constructor(var bytes: ByteArray) : Parcelable {
    var size: Int
    var fileList = mutableListOf<ByteArray>()

    constructor(parcel: Parcel) : this(parcel.createByteArray()!!) {
      size = parcel.readInt()
    }

    init {
      size=bytes[0].toUInt().toInt()
      for (i in  0 until size) {
        fileList.add(bytes.copyOfRange(1+i*16, 17+i*16))
      }
    }

    override fun toString(): String {
      var str = ""
      for (bs in fileList) {
        str += String(bs)
        str += ","
      }
      return str
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
      parcel.writeByteArray(bytes)
      parcel.writeInt(size)
    }

    override fun describeContents(): Int {
      return 0
    }

    companion object CREATOR : Parcelable.Creator<Er1FileList> {
      override fun createFromParcel(parcel: Parcel): Er1FileList {
        return Er1FileList(parcel)
      }

      override fun newArray(size: Int): Array<Er1FileList?> {
        return arrayOfNulls(size)
      }
    }
  }
}
