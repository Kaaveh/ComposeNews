import SwiftUI

@main
struct ComposeNewsApp: App {
    init() {
        initKoin()
    }

    var body: some Scene {
        WindowGroup {
            RootView()
        }
    }
}
