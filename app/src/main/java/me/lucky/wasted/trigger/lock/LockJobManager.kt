package me.lucky.wasted.trigger.lock
import me.lucky.wasted.Preferences


import android.app.job.JobInfo
import android.app.job.JobScheduler
import android.content.ComponentName
import android.content.Context
import java.util.concurrent.TimeUnit

class LockJobManager(private val ctx: Context) {
    companion object {
        private const val JOB_ID = 1000
    }
    private val prefs by lazy { Preferences.new(ctx) }
    private val scheduler = ctx.getSystemService(JobScheduler::class.java)

    fun schedule(): Int {
        return scheduler?.schedule(
            JobInfo.Builder(JOB_ID, ComponentName(ctx, LockJobService::class.java))
                .setMinimumLatency(TimeUnit.MINUTES.toMillis(prefs.triggerLockCount.toLong()))
                .setBackoffCriteria(0, JobInfo.BACKOFF_POLICY_LINEAR)
                .setPersisted(true)
                .build()
        ) ?: JobScheduler.RESULT_FAILURE
    }

    fun cancel() = scheduler?.cancel(JOB_ID)
}