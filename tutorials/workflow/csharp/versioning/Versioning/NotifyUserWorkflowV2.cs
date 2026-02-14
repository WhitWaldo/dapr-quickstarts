using Dapr.Workflow;
using Dapr.Workflow.Versioning;

namespace Versioning;

/// <summary>
/// Identifies a workflow "after" being versioned by name. Note that presence of "V2" in the name identifying this
/// as a later version of the workflow family "NotifyUserWorkflow" - this is
/// because the Dapr .NET Workflow Versioning SDK uses a numerical version strategy by default, but this can
/// be trivially overridden with another built-in strategy or replaced with your own strategy implementing
/// <see cref="IWorkflowVersionStrategy"/>.
///
/// Here, we can refactor to simply remove the patch altogether and exclusively use `SendSmsActivity`.
/// </summary>
public sealed partial class NotifyUserWorkflowV2 : Workflow<string, string>
{
	// Rather than call `SendEmailActivity`, we instead call `SendSmsActivity` in this version of the workflow.
	public override async Task<string> RunAsync(WorkflowContext context, string input)
	{
		var logger = context.CreateReplaySafeLogger<NotifyUserWorkflowV2>();
		LogRunningWorkflow(logger);
		
		return await context.CallActivityAsync<string>(nameof(SendSmsActivity), input);
	}

	[LoggerMessage(LogLevel.Information, "Running v2 of the NotifyUserWorkflow workflow family")]
	private static partial void LogRunningWorkflow(ILogger logger);
}